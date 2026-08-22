# 🚀 Deployment Guide - Campus Resource & Peer Hub

This guide details step-by-step instructions for deploying the Spring Boot backend service to **Google Cloud Run** and the Single Page Application frontend to **Vercel**.

---

## ☁️ 1. Spring Boot Backend Deployment on GCP Cloud Run

### Prerequisites
- Google Cloud SDK (`gcloud` CLI) installed & authenticated.
- Docker installed locally (or Cloud Build enabled).

### Build Container & Deploy to Cloud Run

```bash
# 1. Set GCP Project ID
gcloud config set project YOUR_GCP_PROJECT_ID

# 2. Build Docker Container with Cloud Build
gcloud builds submit --tag gcr.io/YOUR_GCP_PROJECT_ID/campus-hub:latest ./campus-hub

# 3. Deploy Service to Cloud Run
gcloud run deploy campus-hub-api \
  --image gcr.io/YOUR_GCP_PROJECT_ID/campus-hub:latest \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars JWT_SECRET=YourProduction32BytesMinimumSecretKeyHere,GCP_STORAGE_BUCKET=campus-hub-prod-resources
```

---

## ⚡ 2. Frontend Deployment on Vercel

### Automatic Vercel GitHub Integration
1. Go to [Vercel Dashboard](https://vercel.com/dashboard).
2. Click **New Project** and import `aniruthramidi/campus-resource-hub`.
3. Vercel automatically detects [`vercel.json`](vercel.json) and configures:
   - **Build Command**: `npm run build`
   - **Output Directory**: `public`

### Manual Vercel CLI Deployment

```bash
# Install Vercel CLI
npm i -g vercel

# Deploy Preview
vercel

# Deploy Production
vercel --prod
```
