// ===== STAR RATING =====
document.addEventListener('DOMContentLoaded', function () {

    // Star rating interaction
    const stars = document.querySelectorAll('.star');
    const ratingInput = document.getElementById('ratingInput');

    stars.forEach((star, index) => {
        star.addEventListener('click', () => {
            const value = index + 1;
            if (ratingInput) ratingInput.value = value;
            stars.forEach((s, i) => {
                s.classList.toggle('active', i < value);
            });
        });
        star.addEventListener('mouseover', () => {
            stars.forEach((s, i) => s.classList.toggle('active', i <= index));
        });
    });

    const starContainer = document.querySelector('.star-rating');
    if (starContainer) {
        starContainer.addEventListener('mouseleave', () => {
            const val = ratingInput ? parseInt(ratingInput.value) || 0 : 0;
            stars.forEach((s, i) => s.classList.toggle('active', i < val));
        });
    }

    // Auto-dismiss alerts
    const alerts = document.querySelectorAll('.alert-auto-dismiss');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transition = 'opacity 0.5s';
            setTimeout(() => alert.remove(), 500);
        }, 4000);
    });

    // Navbar scroll effect
    const navbar = document.querySelector('.navbar');
    if (navbar) {
        window.addEventListener('scroll', () => {
            navbar.style.boxShadow = window.scrollY > 10
                ? '0 4px 20px rgba(108,99,255,0.15)'
                : '0 2px 8px rgba(108,99,255,0.08)';
        });
    }

    // Animate stat numbers
    const statValues = document.querySelectorAll('.stat-value[data-target]');
    statValues.forEach(el => {
        const target = parseInt(el.getAttribute('data-target'));
        let current = 0;
        const step = Math.ceil(target / 40);
        const timer = setInterval(() => {
            current = Math.min(current + step, target);
            el.textContent = current.toLocaleString();
            if (current >= target) clearInterval(timer);
        }, 30);
    });

    // Mobile navbar toggle
    const menuToggle = document.getElementById('menuToggle');
    const mobileMenu = document.getElementById('mobileMenu');
    if (menuToggle && mobileMenu) {
        menuToggle.addEventListener('click', () => {
            mobileMenu.classList.toggle('open');
        });
    }

    // Ticket count validation
    const ticketInput = document.getElementById('ticketsBooked');
    const maxSeats = document.getElementById('maxSeats');
    if (ticketInput && maxSeats) {
        ticketInput.addEventListener('input', () => {
            const max = parseInt(maxSeats.value);
            const val = parseInt(ticketInput.value);
            if (val > max) {
                ticketInput.value = max;
            }
        });
    }

    // Confirm delete
    const deleteForms = document.querySelectorAll('.delete-form');
    deleteForms.forEach(form => {
        form.addEventListener('submit', (e) => {
            if (!confirm('Are you sure you want to delete this event? This action cannot be undone.')) {
                e.preventDefault();
            }
        });
    });
});
