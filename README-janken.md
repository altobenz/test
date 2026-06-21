# じゃんけんゲーム (Janken)

Apple風のシンプルで洗練されたデザインのじゃんけんゲームです。コンピューターと対戦できます。

## 特徴

- 🎯 グー・チョキ・パーでCPUと対戦
- 📊 スコアボードで勝敗を記録
- ✨ 手を振るアニメーションと勝敗エフェクト
- 🌗 ライト / ダークモード自動対応
- 📱 スマホ・PC両対応のレスポンシブデザイン
- ⌨️ キーボード操作 (1=グー / 2=チョキ / 3=パー)

純粋な HTML / CSS / JavaScript のみで構成された静的サイトです。ビルド不要。

## ローカルで動かす

`index.html` をブラウザで開くだけです。または簡易サーバーで:

```bash
npx serve .
# または
python3 -m http.server 8000
```

## Cloudflare Pages へのデプロイ

このサイトはビルド工程のない静的サイトなので、そのまま Cloudflare Pages に公開できます。

### 方法 A: Git 連携 (推奨)

1. [Cloudflare ダッシュボード](https://dash.cloudflare.com/) にログイン
2. **Workers & Pages** → **Create application** → **Pages** → **Connect to Git**
3. このリポジトリ (`altobenz/test`) を選択
4. ビルド設定を以下のようにする:
   - **Production branch**: `claude/janken-game-site-udphwc` (または master にマージ後 `master`)
   - **Framework preset**: `None`
   - **Build command**: (空欄)
   - **Build output directory**: `/`
5. **Save and Deploy** を押すと数十秒で `https://<project>.pages.dev` に公開されます

以降、ブランチへ push するたびに自動デプロイされます。

### 方法 B: Wrangler CLI で直接アップロード

```bash
npm install -g wrangler
wrangler login
wrangler pages deploy . --project-name=janken
```

> 注: 自動デプロイにはあなたの Cloudflare アカウントの認証が必要です。
> 上記いずれかの手順で公開してください。
