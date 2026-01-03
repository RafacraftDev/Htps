# Htps
Crea y http connection de forma sencilla sin necesidad de ocupar OkHttp o Http Connection.

## Como usarlo?
> aqui abajo esta un código.

```java
try {
    Htps request = new Htps(this, "https://example.com/api", "POST");
    request.setProperty("Content-Type", "application/json; utf-8");
    request.setProperty("Authorization", "Bearer TOKEN_SI_QUIERES");

    String json = "{\"message\":\"Hola\"}";
    request.setContent(json);

    request.getContentAsync(new Htps.ResponseCallback() {
        @Override
        public void onResponse(String response) {
            tv.setText(response);
        }
    });

} catch (Exception e) {
    e.printStackTrace();
}
```
