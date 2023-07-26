

---

# Identity Reconciliation

This application is hosted on Render's free tier, where the server automatically shuts down when not in use. Consequently, the first request processing can take up to 2 minutes, as it requires the server to start from a dormant state. However, once the server is up and running, subsequent requests will be processed instantly.

---
The project involves designing a web service with an endpoint "/identify" to consolidate customer contact information across multiple purchases. The service receives HTTP POST requests with JSON body containing either an email or a phone number.

The service maintains contact information in a relational database table named "Contact." Each customer can have multiple rows in the table, with the oldest row marked as "primary" and others as "secondary." Rows are linked if they share the same email or phone number.

The service's HTTP 200 response contains a JSON payload with the consolidated contact information. It includes the primary contact ID, a list of emails (with the primary contact's email as the first element), a list of phone numbers (with the primary contact's phone number as the first element), and an array of IDs of contacts that are "secondary" to the primary contact.

The web service efficiently handles contact identification and consolidation, ensuring that customer identities are accurately tracked across various orders and interactions.

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

