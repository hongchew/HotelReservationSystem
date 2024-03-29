/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.EmployeeEntity;
import entity.PartnerEntity;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Hong Chew
 */
public class SystemAdministrationModule {
    private EmployeeSessionBeanRemote employeeSessionBean;
    private PartnerSessionBeanRemote partnerSessionBean;
    private EmployeeEntity sysAdmin;
    private Scanner sc;
    
    public SystemAdministrationModule() {
        this.sc = new Scanner(System.in);
    }

    public SystemAdministrationModule(EmployeeEntity sysAdmin, EmployeeSessionBeanRemote employeeSessionBean, PartnerSessionBeanRemote partnerSessionBean) {
        this();
        
        this.sysAdmin = sysAdmin;
        this.employeeSessionBean = employeeSessionBean;
        this.partnerSessionBean = partnerSessionBean;
    }
    
    
    /*
    Navigation menu for SystemAdminEntity when they log into the Management Client.
    Allows sysadmins to access methods to create employee & partners, and view all employess & partners
    */
    public void runSysAdminModule(){
        
        while(true){
            System.out.println("\n****Welcome to the System Administration Module****");
            System.out.println("(1) Create New Employee \n(2) View All Employee \n(3) Create New Partner \n(4) View All Partners \n(5) Return");
            String response = sc.next();
            switch(response){
                case "1":
                    createEmployee();
                    break;
                case "2":
                    viewAllEmployees();
                    break;
                case "3":
                    createPartner();
                    break;
                case "4":
                    viewAllPartners();
                    break;
                case "5":
                    System.out.println("\n****Logging Out****\n");
                    return;
                default:
                    System.err.println("\nInvalid input please try again.\n");
            }
        }
    }
    
    /*
    method for sysadmin to create a new employee (Sysadmin, ops manager, sales manager, guest relation offr) account 
    */
    public void createEmployee(){
        System.out.println("**** Create a new Employee Account****");
        System.out.println("Enter employee's name:");
        sc.nextLine();
        String name = sc.nextLine();
        System.out.println("\nEnter username:");
        String username = sc.nextLine();
        System.out.println("\nEnter password:");
        String password = sc.nextLine();
        
        System.out.println("\nCreate new employee as:\n(1)System Administrator \n(2)Operation Manager \n(3)Sales Manager \n(4)Guest Relation Officer \n(5)Cancel");
        String response = sc.next();
        switch(response){
            case "1":
                employeeSessionBean.createNewSysAdmin(name, username, password);
                System.out.println("New System Administrator created.\n");                    
                break;
            case "2":
                employeeSessionBean.createNewOpsManager(name, username, password);
                System.out.println("New Operation Manager created.\n");
                break;
            case "3":
                employeeSessionBean.createNewSalesManager(name, username, password);
                System.out.println("New Sales Manager created.\n");
                break;
            case "4":
                employeeSessionBean.createNewGuestRelationsOffr(name, username, password);
                System.out.println("New Guest Relations Officer created.\n");
                break;
            case "5":
                return;
            default:
                System.err.println("Invalid input please try again.\n");
        }

    }
    
    /*
    Method for sysadmin to retrieve and view all employees in database (id and name)
    */
    public void viewAllEmployees(){
        List<EmployeeEntity> list = employeeSessionBean.retrieveAllEmployees();
        System.out.println("\n****Viewing All Employees****");
        for(EmployeeEntity e: list){
            System.out.println(e.toString());
        }
        System.out.println("****End of List****\n");
    }
    
    /*
    Method for sysadmin to create new partner account 
    */
    public void createPartner(){
        System.out.println("**** Create a new Partner Account****");
        System.out.println("Enter partners's name:");
        sc.nextLine();
        String name = sc.nextLine();
        System.out.println("Enter username:");
        String username = sc.nextLine();
        System.out.println("Enter password:");
        String password = sc.nextLine();
        
        partnerSessionBean.createNewPartner(name, username, password);
        System.out.println("New partner created.");
    }
    
    /*
    Method for sysadmin to retrieve and view all partners in database (id and name)
    */
    public void viewAllPartners(){
        List<PartnerEntity> list = partnerSessionBean.retrieveAllPartners();
        System.out.println("\n****Viewing All Partners****");
        for(PartnerEntity p: list){
            System.out.println(p.toString());
        }
        System.out.println("****End of List****\n");
    }
}
