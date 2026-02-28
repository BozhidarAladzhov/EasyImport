document.addEventListener('DOMContentLoaded', function () {
  // 1) Auto-mark elements for reveal animations
  const autoSelectors = [
    '.section',
    '.section .card',
    '.section .content p',
    '.section h2, .section h3, .section h4',
    '.form-shell',
    '.table',
    '.stats .stat',
    '.about__highlights li'
  ];

  // Mark sections so inner elements can be revealed
  document.querySelectorAll('.section').forEach(sec => sec.classList.add('reveal'));

  // Mark elements by selectors above
  autoSelectors.forEach(sel => {
    document.querySelectorAll(sel).forEach(el => {
      el.classList.add('reveal');
    });
  });

  // 2) Stagger groups: rows/grids
  const staggerGroups = [
    '.cards-equal .row, .cards-equal',
    '.process-steps .row',
    '.about__highlights',
    '.table.reveal-rows tbody'
  ];
  staggerGroups.forEach(groupSel => {
    document.querySelectorAll(groupSel).forEach(group => {
      group.classList.add('reveal-stagger');
      [...group.children].forEach((child, i) => {
        child.style.setProperty('--i', i);
      });
    });
  });

  // 3) IntersectionObserver
  const prefersReduced = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  if (prefersReduced) {
    document.querySelectorAll('.reveal, .reveal-stagger').forEach(el => el.classList.add('is-visible'));
    return;
  }

  const io = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('is-visible');
        io.unobserve(entry.target);
      }
    });
  }, { rootMargin: '0px 0px -10% 0px', threshold: 0.15 });

  document.querySelectorAll('.reveal, .reveal-stagger, .section').forEach(el => io.observe(el));

  // 4) Bonus: stagger table rows
  document.querySelectorAll('.table').forEach(tb => tb.classList.add('reveal-rows'));
});
