package by.prus.LabProject.model.response;

/**
 * Class needed for translation message in rest API.
 * For example delete of entity. This class convert to JSON and
 * tells about successful or no operation
 */
public class OperationStatusModel {

    private String operationResult;
    private String operationName;

    public String getOperationResult() { return operationResult; }
    public void setOperationResult(String operationResult) { this.operationResult = operationResult; }
    public String getOperationName() { return operationName; }
    public void setOperationName(String operationName) { this.operationName = operationName; }

}
