/**
 * Dashboard JavaScript
 * Manages displaying and interacting with leave requests
 */

const API_BASE = '/api/leave';

async function loadRequests() {
    const loading = document.getElementById('loading');
    const requestsList = document.getElementById('requestsList');
    const emptyState = document.getElementById('emptyState');
    const requestsTable = document.getElementById('requestsTable');

    loading.style.display = 'block';
    requestsList.style.display = 'none';
    emptyState.style.display = 'none';

    try {
        const response = await fetch(`${API_BASE}/list`);
        const requests = await response.json();

        loading.style.display = 'none';

        if (requests.length === 0) {
            emptyState.style.display = 'block';
        } else {
            requestsList.style.display = 'block';
            updateStats(requests);
            displayRequests(requests, requestsTable);
        }
    } catch (error) {
        loading.innerHTML = `<p style="color: red;">❌ Gagal memuat data: ${error.message}</p>`;
        console.error('Error loading requests:', error);
    }
}

function updateStats(requests) {
    const total = requests.length;
    const submitted = requests.filter(r => r.status === 'DIAJUKAN').length;
    const approved = requests.filter(r =>
        r.status === 'DISETUJUI_HR' || r.status === 'DISETUJUI_ATASAN'
    ).length;
    const rejected = requests.filter(r => r.status === 'DITOLAK').length;

    document.getElementById('totalCount').textContent = total;
    document.getElementById('submittedCount').textContent = submitted;
    document.getElementById('approvedCount').textContent = approved;
    document.getElementById('rejectedCount').textContent = rejected;
}

function displayRequests(requests, table) {
    table.innerHTML = '';

    requests.forEach(req => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${escapeHtml(req.id.substring(0, 8))}</td>
            <td>${escapeHtml(req.nama)}</td>
            <td>${req.mulai} s/d ${req.selesai}</td>
            <td>${req.jumlahHari} hari</td>
            <td>${req.sisaCuti} hari</td>
            <td><span class="status-badge ${req.status.toLowerCase()}">${req.statusLabel}</span></td>
            <td>
                <div class="action-buttons">
                    <button class="btn btn-primary" onclick="showDetail('${req.id}')">Detail</button>
                </div>
            </td>
        `;
        table.appendChild(row);
    });
}

async function showDetail(requestId) {
    const modal = document.getElementById('detailModal');
    const modalBody = document.getElementById('modalBody');

    try {
        const response = await fetch(`${API_BASE}/${requestId}`);
        const req = await response.json();

        let actions = '';

        // Generate available actions based on current status
        if (req.status === 'DRAFT') {
            actions = `
                <div style="margin-top: 1rem; display: flex; gap: 0.5rem; flex-wrap: wrap;">
                    <button class="btn btn-success" onclick="doTransition('${req.id}', 'ajukan')">Ajukan</button>
                    <button class="btn btn-danger" onclick="doTransition('${req.id}', 'batalkan')">Batalkan</button>
                </div>
            `;
        } else if (req.status === 'DIAJUKAN') {
            actions = `
                <div style="margin-top: 1rem; display: flex; gap: 0.5rem; flex-wrap: wrap;">
                    <button class="btn btn-success" onclick="doTransition('${req.id}', 'approve_atasan')">Setujui (Atasan)</button>
                    <button class="btn btn-danger" onclick="showRejectModal('${req.id}')">Tolak</button>
                </div>
            `;
        } else if (req.status === 'DISETUJUI_ATASAN') {
            actions = `
                <div style="margin-top: 1rem; display: flex; gap: 0.5rem; flex-wrap: wrap;">
                    <button class="btn btn-success" onclick="doTransition('${req.id}', 'approve_hr')">Setujui (HR)</button>
                </div>
            `;
        }

        const alasanHtml = req.alasanTolak ?
            `<div class="modal-body-item">
                <label>Alasan Penolakan</label>
                <div class="value" style="color: #ef4444;">${escapeHtml(req.alasanTolak)}</div>
            </div>` : '';

        modalBody.innerHTML = `
            <div class="modal-body-item">
                <label>ID Pengajuan</label>
                <div class="value">${escapeHtml(req.id)}</div>
            </div>
            <div class="modal-body-item">
                <label>Nama</label>
                <div class="value">${escapeHtml(req.nama)}</div>
            </div>
            <div class="modal-body-item">
                <label>Email</label>
                <div class="value">${escapeHtml(req.email)}</div>
            </div>
            <div class="modal-body-item">
                <label>Tanggal</label>
                <div class="value">${req.mulai} s/d ${req.selesai}</div>
            </div>
            <div class="modal-body-item">
                <label>Jumlah Hari</label>
                <div class="value">${req.jumlahHari} hari</div>
            </div>
            <div class="modal-body-item">
                <label>Sisa Cuti</label>
                <div class="value">${req.sisaCuti} hari</div>
            </div>
            <div class="modal-body-item">
                <label>Status</label>
                <div class="value">
                    <span class="status-badge ${req.status.toLowerCase()}">${req.statusLabel}</span>
                </div>
            </div>
            <div class="modal-body-item">
                <label>Dibuat Pada</label>
                <div class="value">${req.dibuat}</div>
            </div>
            ${alasanHtml}
            <div class="modal-actions">
                ${actions}
                <button class="btn btn-secondary" onclick="closeModal()">Tutup</button>
            </div>
        `;

        modal.style.display = 'block';
    } catch (error) {
        alert('Gagal memuat detail: ' + error.message);
    }
}

async function doTransition(requestId, transisi) {
    if (!confirm(`Lanjutkan transisi "${transisi}"?`)) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/${requestId}/transisi`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ transisi: transisi })
        });

        const result = await response.json();

        if (result.sukses) {
            alert(`✓ ${result.pesan}`);
            closeModal();
            loadRequests();
        } else {
            alert(`❌ Gagal: ${result.pesan}`);
        }
    } catch (error) {
        alert('Error: ' + error.message);
    }
}

function showRejectModal(requestId) {
    const alasan = prompt('Masukkan alasan penolakan:');
    if (alasan) {
        doTransitionWithReason(requestId, 'tolak', alasan);
    }
}

async function doTransitionWithReason(requestId, transisi, alasan) {
    try {
        const response = await fetch(`${API_BASE}/${requestId}/transisi`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ transisi: transisi, alasanTolak: alasan })
        });

        const result = await response.json();

        if (result.sukses) {
            alert(`✓ ${result.pesan}`);
            closeModal();
            loadRequests();
        } else {
            alert(`❌ Gagal: ${result.pesan}`);
        }
    } catch (error) {
        alert('Error: ' + error.message);
    }
}

function closeModal() {
    document.getElementById('detailModal').style.display = 'none';
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Close modal when clicking outside
window.onclick = function (event) {
    const modal = document.getElementById('detailModal');
    if (event.target == modal) {
        modal.style.display = 'none';
    }
}
