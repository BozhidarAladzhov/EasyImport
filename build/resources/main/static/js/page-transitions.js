(function () {
  const body = document.body;

  // Взимаме реалната продължителност от CSS transition на body
  function getTransitionMs() {
    const cs = getComputedStyle(body);
    // Може да има множество durations, вземаме най-голямата
    const durs = cs.transitionDuration.split(',').map(s => s.trim());
    const delays = cs.transitionDelay.split(',').map(s => s.trim());
    const toMs = (v) => v.endsWith('ms') ? parseFloat(v) : parseFloat(v) * 1000;
    const list = durs.map((d, i) => toMs(d) + (delays[i] ? toMs(delays[i]) : 0));
    return Math.max(...list, 0) || 400;
  }

  // Маркираме елементи за stagger reveal (по-силен ефект)
  const BASE_DELAY = 150;
  const STEP_DELAY = 70;
  const REVEAL_SELECTORS = [
    'h1','h2','h3','h4','.lead','p','li',
    '.card','.form-shell','.table',
    '.nav-link','.btn'
  ].join(',');

  function tagForReveal(scope = document) {
    const items = Array.from(scope.querySelectorAll(REVEAL_SELECTORS));
    items.forEach((el, i) => {
      el.classList.add('reveal-item');
      el.style.setProperty('--d', (BASE_DELAY + i * STEP_DELAY) + 'ms');
    });
  }

  // Старт на входната анимация (fade-in)
  function runEnterAnimation() {
    // body вече има .page-fade от HTML — само добавяме stagger и .ready
    tagForReveal(document);
    // Следващ кадър, за да „хване“ transition-а
    requestAnimationFrame(() => body.classList.add('ready'));
  }

  // Поддръжка за Back/Forward cache
  window.addEventListener('pageshow', (e) => {
    // при връщане от bfcache махаме exiting/ready и рестартираме
    body.classList.remove('exiting', 'ready');
    document.querySelectorAll('.reveal-item').forEach(el => {
      el.classList.remove('reveal-item');
      el.style.removeProperty('--d');
    });
    runEnterAnimation();
  });

  // Първоначален вход
  runEnterAnimation();

  // Click-делегация: плавен exit само за вътрешни линкове
  document.addEventListener('click', function (e) {
    const a = e.target.closest('a');
    if (!a) return;

    // Игнор: модификатори, среден бутон, target≠_self, download, anchor-only (#), друг origin
    if (e.defaultPrevented || e.button !== 0 || e.metaKey || e.ctrlKey || e.shiftKey || e.altKey) return;
    if (a.target && a.target !== '_self') return;
    if (a.hasAttribute('download')) return;
    const href = a.getAttribute('href') || '';
    if (href.startsWith('#')) return;

    const url = new URL(a.href, location.href);
    if (url.origin !== location.origin) return;

    // Ако целевият URL е същият като текущия (някои „активни“ линкове),
    // няма смисъл да анимираме изход — просто стоп.
    if (url.pathname === location.pathname && url.search === location.search && url.hash === '') return;

    e.preventDefault();
    body.classList.remove('ready'); // предотвратява „мигване“
    body.classList.add('exiting');

    const DURATION = getTransitionMs(); // синхронизирано с CSS
    setTimeout(() => { location.href = a.href; }, DURATION);
  });

  // При истински unload (refresh/close) — маркираме exiting (някои браузъри уважават)
  window.addEventListener('beforeunload', () => {
    body.classList.remove('ready');
    body.classList.add('exiting');
  });

  // Safari/Chrome: pagehide при bfcache. Ако не се кешира (persisted=false), това е реален unload.
  window.addEventListener('pagehide', (e) => {
    if (!e.persisted) {
      body.classList.remove('ready');
      body.classList.add('exiting');
    }
  });
})();
