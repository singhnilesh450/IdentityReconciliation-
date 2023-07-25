

---

# Identity Reconciliation

This application is hosted on Render's free tier, where the server automatically shuts down when not in use. Consequently, the first request processing can take up to 2 minutes, as it requires the server to start from a dormant state. However, once the server is up and running, subsequent requests will be processed instantly.

---

### Endpoint

**POST request**: [https://identity-recon-pqxg.onrender.com/identify](https://identity-recon-pqxg.onrender.com/identify)

**Request Body Example**:

```json
{
  "email": "lorraine@hillvalley.edu",
  "phoneNumber": "123456"
}
```

**Response Body Example**:

```json
{
    "contact": {
        "primaryContactId": 1,
        "emails": [
            "lorraine@hillvalley.edu"
        ],
        "phoneNumbers": [
            "123456"
        ],
        "secondaryContactIds": []
    }
}
```

---

