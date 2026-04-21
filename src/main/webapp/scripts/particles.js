(function () {
    const canvas = document.getElementById('bg-canvas');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');

    function resize() {
        canvas.width  = window.innerWidth;
        canvas.height = window.innerHeight;
    }
    resize();
    window.addEventListener('resize', resize);

    const COLORS = [
        [139, 26,  26],
        [170, 45,  15],
        [200, 70,  10],
        [110, 20,  20],
        [220, 100, 30],
    ];

    function Particle(initial) {
        this.reset(initial);
    }

    Particle.prototype.reset = function (initial) {
        this.x          = Math.random() * canvas.width;
        this.y          = initial ? Math.random() * canvas.height : canvas.height + 8;
        this.size       = Math.random() * 2.2 + 0.4;
        this.speedY     = Math.random() * 0.55 + 0.12;
        this.drift      = (Math.random() - 0.5) * 0.2;
        this.wobble     = Math.random() * Math.PI * 2;
        this.wobbleSpd  = Math.random() * 0.022 + 0.006;
        this.life       = 0;
        this.maxLife    = Math.random() * 280 + 160;
        const c         = COLORS[Math.floor(Math.random() * COLORS.length)];
        this.r = c[0]; this.g = c[1]; this.b = c[2];
    };

    Particle.prototype.update = function () {
        this.life++;
        this.wobble += this.wobbleSpd;
        this.x      += this.drift + Math.sin(this.wobble) * 0.4;
        this.y      -= this.speedY;
        if (this.life >= this.maxLife || this.y < -12) this.reset(false);
    };

    Particle.prototype.draw = function () {
        const p = this.life / this.maxLife;
        let a   = p < 0.2 ? p / 0.2 : p > 0.78 ? (1 - p) / 0.22 : 1;
        a      *= 0.7;
        const r = this.size * 5;
        const g = ctx.createRadialGradient(this.x, this.y, 0, this.x, this.y, r);
        g.addColorStop(0,   `rgba(${this.r},${this.g},${this.b},${a})`);
        g.addColorStop(0.35,`rgba(${this.r},${this.g},${this.b},${a * 0.35})`);
        g.addColorStop(1,   `rgba(${this.r},${this.g},${this.b},0)`);
        ctx.beginPath();
        ctx.arc(this.x, this.y, r, 0, Math.PI * 2);
        ctx.fillStyle = g;
        ctx.fill();
    };

    const COUNT      = 55;
    const particles  = [];
    for (let i = 0; i < COUNT; i++) particles.push(new Particle(true));

    function loop() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        for (let i = 0; i < particles.length; i++) {
            particles[i].update();
            particles[i].draw();
        }
        requestAnimationFrame(loop);
    }
    loop();
})();
