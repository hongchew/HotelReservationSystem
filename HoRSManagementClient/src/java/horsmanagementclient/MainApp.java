/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.singleton.SystemHelperRemote;
import ejb.session.stateful.RoomReservationControllerRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Hong Chew
 */
public class MainApp {
    private RoomSessionBeanRemote roomSessionBean;
    private EmployeeSessionBeanRemote employeeSessionBean;
    private PartnerSessionBeanRemote partnerSessionBean;
    private RoomReservationControllerRemote roomReservationController;
    private SystemHelperRemote systemHelper;
    private EmployeeEntity currentEmployee;
    private Scanner sc;
    
    public MainApp() {
        this.sc = new Scanner(System.in);
    }

    public MainApp(RoomSessionBeanRemote roomSessionBean, EmployeeSessionBeanRemote employeeSessionBean, PartnerSessionBeanRemote partnerSessionBean, RoomReservationControllerRemote roomReservationController, SystemHelperRemote systemHelper) {
        this();
        
        this.roomSessionBean = roomSessionBean;
        this.employeeSessionBean = employeeSessionBean;
        this.partnerSessionBean = partnerSessionBean;
        this.roomReservationController = roomReservationController;
        this.systemHelper = systemHelper;
    }
    
    
    public void runApp(){
        
        while(true){
            System.out.println("****Welcome to Merlion Hotel****");
            System.out.println("(1) Login \n(2) Exit");
            String response = sc.next();
            switch(response){
                case "1":
                    doLogin();
                    break;
                case "2":
                    System.exit(0);
                default:
                    System.out.println("Please enter a valid command");
            }
        }
    }
    
    public void doLogin(){  
        System.out.println("Enter Username:");
        String username = sc.next();
        System.out.println("Enter password:");
        String password = sc.next();
        
        try {
            currentEmployee = employeeSessionBean.login(username, password);
            System.out.println("\nLogin successful.\n");
            employeeMenu();
        }catch (InvalidLoginCredentialException ex) {
            System.err.println("\nInvalid Credentials. Please Try Again.\n");
        } 
    }
    
    public void employeeMenu(){

        
        String response;
        switch(currentEmployee.getAccessRights()){
            case SYSADMIN:
                while(true){
                    System.out.println("\n****Welcome to the Hotel Management Client****");
                    System.out.println("(1)System Administrator Module \n(2)Logout");
                    response = sc.next();
                    if(response.equals("1")){
                        SystemAdministrationModule sysAdminModule = new SystemAdministrationModule(currentEmployee ,employeeSessionBean, partnerSessionBean);
                        sysAdminModule.runSysAdminModule();
                    }else if(response.equals("2")){
                        doLogout();
                        break;
                    }else{
                        System.out.println("Enter a valid command");
                    }
                }
                break;
                
            case OPERATIONS:
                while(true){
                    System.out.println("\n****Welcome to the Hotel Management Client****");
                    System.out.println("(1)Hotel Operation (Operation Manager) Module \n(2)Logout");
                    response = sc.next();
                    if(response.equals("1")){
                        HotelOperationModule opsMod = new HotelOperationModule(roomSessionBean, systemHelper, currentEmployee);
                        opsMod.runHotelOperationModuleOperationsManager();
                    }else if(response.equals("2")){
                        doLogout();
                        break;
                    }else{
                        System.out.println("Enter a valid command");
                    }
                }
                break;
                
            case SALES:
                while(true){
                    System.out.println("\n****Welcome to the Hotel Management Client****");
                    System.out.println("(1)Hotel Operation Module (Sales Manager) \n(2)Logout");
                    response = sc.next();
                    if(response.equals("1")){
                        HotelOperationModule opsMod = new HotelOperationModule(roomSessionBean, systemHelper, currentEmployee);
                        opsMod.runHotelOperationModuleSalesManager();
                    }else if(response.equals("2")){
                        doLogout();
                        break;
                    }else{
                        System.out.println("Enter a valid command");
                    }
                }
                break;
                
            case GUESTRELATIONS:
                while(true){
                    System.out.println("\n****Welcome to the Hotel Management Client****");
                    System.out.println("(1)Front Office Module \n(2)Logout");
                    response = sc.next();
                    if(response.equals("1")){
                        FrontOfficeModule front = new FrontOfficeModule(roomReservationController);
                        front.runFrontOfficeModule();
                    }else if(response.equals("2")){
                        doLogout();
                        break;
                    }else{
                        System.out.println("Enter a valid command");
                    }
                }
                break;

            default:
                while(true){
                    System.out.println("(1) Log Out");
                    if(sc.next().equals("1")){
                        doLogout();
                        return;
                    }
                }
        }
    }
    
    public void doLogout(){
        currentEmployee = null;
        System.out.println("Logout successful.");
    }
    
}
