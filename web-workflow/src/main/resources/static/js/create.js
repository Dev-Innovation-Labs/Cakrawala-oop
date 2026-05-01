/**
 * Create Page JavaScript
 * Handles form submission for creating new leave requests
 */

const API_BASE = '/api/leave';

document.getElementById('leaveForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const nama = document.getElementById('nama').value.trim();
    const email = document.getElementById('email').value.trim();
    const mulai = document.getElementById('mulai').value;
    const selesai = document.getElementById('selesai').value;
    const sisaCuti = parseInt(document.getElementById('sisaCuti').value);

    // Reset messages
    document.getElementById('successMessage').style.display = 'none';
    document.getElementById('errorMessage').style.display = 'none';

    // Validation
    if (!nama || !email || !mulai || !selesai || !sisaCuti) {
        showError('Semua field harus diisi');
        return;
    }

    if (sisaCuti < 0) {
        showError('Sisa cuti tidak boleh negatif');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/create`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                nama: nama,
                email: email,
                mulai: mulai,
                selesai: selesai,
                sisaCuti: sisaCuti
            })
        });

        const data = await response.json();

        if (response.status === 201) {
            showSuccess(data);
            document.getElementById('leaveForm').reset();
        } else {
            showError(data.error || 'Gagal membuat pengajuan');
        }
    } catch (error) {
        showError('Error: ' + error.message);
        console.error('Error creating request:', error);
    }
});

function showSuccess(data) {
    const successMsg = document.getElementById('successMessage');
    document.getElementById('requestId').textContent = data.id;
    document.getElementById('nextSteps').textContent =
        'Pengajuan Anda telah berhasil dibuat dengan status DRAFT. ' +
        'Klik "Lihat di Dashboard" atau lakukan transisi "Ajukan" untuk mengirim ke atasan.';

    successMsg.style.display = 'block';
    document.getElementById('errorMessage').style.display = 'none';

    // Scroll to message
    successMsg.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

function showError(message) {
    document.getElementById('errorText').textContent = message;
    document.getElementById('errorMessage').style.display = 'block';
    document.getElementById('successMessage').style.display = 'none';

    // Scroll to message
    document.getElementById('errorMessage').scrollIntoView({ behavior: 'smooth', block: 'center' });
}

function goToDashboard() {
    window.location.href = '/dashboard';
}
