package src;

public interface CustomerManager {
//    void addCustomer();

    void addCustomer();

    void updateCustomer();






    void updateDependentInfoAndUpdateCustomer(String dependentId, Customer parentCustomer);

    void deleteCustomer();

    void getCustomerById();

    void getAllCustomers();


//    void addDependent(Customer policyHolder);

    void getAllDependents();
}
