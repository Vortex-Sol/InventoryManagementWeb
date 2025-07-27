package vortex.imwp.Models;

public class Response {
    private Object data;
    private String message;
    private boolean success;

    public Response() {}
    public Response(Object data) {}
    public Response(Object data, String message) {
        this.data = data;
        this.message = message;
    }
    public Response(Object data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public Object getData() { return data; }
    public String getMessage() { return message; }
    public boolean isSuccess() { return success; }

    public void setData(Object data) { this.data = data; }
    public void setMessage(String message) { this.message = message; }
    public void setSuccess(boolean success) { this.success = success; }
}
