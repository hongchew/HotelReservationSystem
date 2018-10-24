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
    private Scanner sc;
    
    public SystemAdministrationModule() {
        this.sc = new Scanner(System.in);
    }

    public SystemAdministrationModule(EmployeeSessionBeanRemote employeeSessionBean) {
        this.employeeSessionBean = employeeSessionBean;
    }
    
    
    
    public void runSysAdminModule(){
        System.out.println("****Welcome to the System Administration Module****\n");
        while(true){
            System.out.println("(1) Create New Employee \n(2) View All Employee \n(3) Create New Partner \n(4) View All Partners \n(5) Exit System");
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
                    System.exit(0);
                default:
                    System.err.println("Invalid input please try again.");
            }
        }
    }
    
    public void createEmployee(){
        System.out.println("**** Create a new Employee Account****");
        System.out.println("Enter employee's name:");
        String name = sc.nextLine();
        System.out.println("Enter username:");
        String username = sc.nextLine();
        System.out.println("Enter password:");
        String password = sc.nextLine();
        
        while(true){
            System.out.println("(1)System Administrator \n(2)Operation Manager \n(3)Sales Manager \n(4)Guest Relation Officer \n(5)Return");
            String response = sc.next();
            switch(response){
                case "1":
                    employeeSessionBean.createNewSysAdmin(name, username, password);
                    System.out.println("New System Administrator created.");                    
                    break;
                case "2":
                    employeeSessionBean.createNewOpsManager(name, username, password);
                    System.out.println("New Operation Manager created.");
                    break;
                case "3":
                    employeeSessionBean.createNewSalesManager(name, username, password);
                    System.out.println("New Sales Manager created.");
                    break;
                case "4":
                    employeeSessionBean.createNewGuestRelationsOffr(name, username, password);
                    System.out.println("New Guest Relations Officer created.");
                    break;
                case "5":
                    return;
                default:
                    System.err.println("Invalid input please try again.");
            }
        }
    }
    
    public void viewAllEmployees(){
        List<EmployeeEntity> list = employeeSessionBean.retrieveAllEmployees();
        for(EmployeeEntity e: list){
            System.out.println(e.toString());
        }
    }
    
    public void createPartner(){
        System.out.println("**** Create a new Partner Account****");
        System.out.println("Enter partners's name:");
        String name = sc.nextLine();
        System.out.println("Enter username:");
        String username = sc.nextLine();
        System.out.println("Enter password:");
        String password = sc.nextLine();
        
        partnerSessionBean.createNewPartner(name, username, password);
        System.out.println("New partner created.");
    }
    
    public void viewAllPartners(){
        List<PartnerEntity> list = partnerSessionBean.retrieveAllPartners();
        for(PartnerEntity p: list){
            System.out.println(p.toString());
        }
    }
}
