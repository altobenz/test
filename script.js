(() => {
  "use strict";

  const HANDS = {
    rock: { emoji: "✊", name: "グー", beats: "scissors" },
    scissors: { emoji: "✌️", name: "チョキ", beats: "paper" },
    paper: { emoji: "✋", name: "パー", beats: "rock" },
  };
  const KEYS = Object.keys(HANDS);

  const el = {
    playerEmoji: document.getElementById("playerEmoji"),
    cpuEmoji: document.getElementById("cpuEmoji"),
    playerName: document.getElementById("playerName"),
    cpuName: document.getElementById("cpuName"),
    playerHand: document.getElementById("playerHand"),
    cpuHand: document.getElementById("cpuHand"),
    playerScore: document.getElementById("playerScore"),
    cpuScore: document.getElementById("cpuScore"),
    result: document.getElementById("result"),
    reset: document.getElementById("reset"),
    choices: Array.from(document.querySelectorAll(".choice")),
  };

  el.cpuHand.classList.add("cpu");

  let playerScore = 0;
  let cpuScore = 0;
  let busy = false;

  function setChoicesDisabled(disabled) {
    el.choices.forEach((b) => (b.disabled = disabled));
  }

  function clearStates() {
    [el.playerHand, el.cpuHand].forEach((c) => {
      c.classList.remove("winner", "loser", "shake");
    });
    el.result.classList.remove("win", "lose", "draw");
  }

  function play(playerKey) {
    if (busy) return;
    busy = true;
    setChoicesDisabled(true);
    clearStates();

    const cpuKey = KEYS[Math.floor(Math.random() * KEYS.length)];

    // Shaking suspense animation
    el.result.textContent = "じゃんけん…";
    el.playerHand.classList.add("shake");
    el.cpuHand.classList.add("shake");
    el.playerEmoji.textContent = "✊";
    el.cpuEmoji.textContent = "✊";

    window.setTimeout(() => {
      el.playerHand.classList.remove("shake");
      el.cpuHand.classList.remove("shake");
      reveal(playerKey, cpuKey);
    }, 650);
  }

  function reveal(playerKey, cpuKey) {
    const player = HANDS[playerKey];
    const cpu = HANDS[cpuKey];

    el.playerEmoji.textContent = player.emoji;
    el.cpuEmoji.textContent = cpu.emoji;
    el.playerName.textContent = player.name;
    el.cpuName.textContent = cpu.name;

    let outcome;
    if (playerKey === cpuKey) {
      outcome = "draw";
    } else if (player.beats === cpuKey) {
      outcome = "win";
    } else {
      outcome = "lose";
    }

    if (outcome === "win") {
      playerScore++;
      el.result.textContent = "あなたの勝ち！";
      el.playerHand.classList.add("winner");
      el.cpuHand.classList.add("loser");
    } else if (outcome === "lose") {
      cpuScore++;
      el.result.textContent = "あなたの負け…";
      el.cpuHand.classList.add("winner");
      el.playerHand.classList.add("loser");
    } else {
      el.result.textContent = "あいこ";
    }

    el.result.classList.add(outcome);
    el.playerScore.textContent = String(playerScore);
    el.cpuScore.textContent = String(cpuScore);

    busy = false;
    setChoicesDisabled(false);
  }

  function reset() {
    playerScore = 0;
    cpuScore = 0;
    busy = false;
    clearStates();
    setChoicesDisabled(false);
    el.playerScore.textContent = "0";
    el.cpuScore.textContent = "0";
    el.playerEmoji.textContent = "✊";
    el.cpuEmoji.textContent = "✊";
    el.playerName.textContent = "あなた";
    el.cpuName.textContent = "CPU";
    el.result.textContent = "手を選んでください";
  }

  el.choices.forEach((btn) => {
    btn.addEventListener("click", () => play(btn.dataset.choice));
  });
  el.reset.addEventListener("click", reset);

  // Keyboard shortcuts: 1=グー, 2=チョキ, 3=パー
  document.addEventListener("keydown", (e) => {
    const map = { 1: "rock", 2: "scissors", 3: "paper" };
    if (map[e.key]) play(map[e.key]);
  });
})();
