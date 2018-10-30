/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.RoomSessionBeanRemote;
import entity.EmployeeEntity;
import entity.RoomTypeEntity;
import java.util.*;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author Hong Chew
 */
public class HotelOperationModule {

    private RoomSessionBeanRemote roomSessionBean;
    
    private EmployeeEntity currentEmployee;
    private Scanner sc = new Scanner(System.in);
    
    public HotelOperationModule() {
    }

    public HotelOperationModule(RoomSessionBeanRemote roomSessionBean, EmployeeEntity currentEmployee) {
        this.roomSessionBean = roomSessionBean;
        this.currentEmployee = currentEmployee;
    }
    
    
    public void runHotelOperationModuleOperationsManager(){
        System.out.println("****Welcome to the Hotel Operations Module****");
        while(true){
            System.out.println("(1) Room Type Management");
            System.out.println("(2) Room Management");
            System.out.println("(3) View Room Allocation Exception Report");
            System.out.println("(4) Return");
            String response = sc.next();
            switch(response){
                case "1":
                    roomTypeManagement();
                    break;
                    
                case "2":
                    roomManagement();
                    break;
                    
                case "3":
                    viewExceptionReport();
                    break;
                    
                case "4":
                    return;
 
                default:
                    System.err.println("Please enter a valid command");
            }           
        }
    }
    
    
    private void roomTypeManagement(){
        while(true){
            System.out.println("(1) Create new Room Type");
            System.out.println("(2) View/Update/Delete Room Type Details");
            System.out.println("(3) View All Room Types");
            System.out.println("(4) Return");
            
            String response = sc.next();
            switch(response){
                case "1":
                    createNewRoomType();
                    break;
                    
                case "2":
                    viewRoomTypeDetails();
                    break;
                    
                case "3":
                    viewAllRoomTypes();
                    break;
                    
                case "4":
                    return;
                                      
                default:
                    System.err.println("Please input a valid command.");
            }
            
        }
    }
    
    private void createNewRoomType(){
        System.out.println("Enter name of new Room Type");
        String newTypeName = sc.nextLine();
        
        System.out.println("Enter 140 characters description for " + newTypeName);
        String newDescription = sc.nextLine();
        
        System.out.println("Enter types of bed available in " + newTypeName);
        String newBeds = sc.nextLine();
        
        System.out.println("Enter capacity of " + newTypeName);
        Integer capacity = sc.nextInt();
        
        System.out.println("Enter amenities available in " + newTypeName);
        String newAmenities = sc.nextLine();
        
        roomSessionBean.createNewRoomType(newTypeName, newDescription, newBeds, capacity, newAmenities);
        System.out.println(newTypeName + " created successfully!");
    }
    
    private void viewRoomTypeDetails(){
        List<RoomTypeEntity> roomTypes = viewAllRoomTypes();
        System.out.println("Input Room Type number to view/edit details or to delete");
        int typeNum = sc.nextInt();
        if(typeNum >= roomTypes.size()){
            System.out.println("Invalid number, returning to main menu");
            return;
        }else{
            String type = roomTypes.get(typeNum).getTypeName();
            System.out.println(roomSessionBean.viewRoomTypeDetails(type));
            
            System.out.println("(1) Edit Details");
            System.out.println("(2) Delete Room Type");
            String response = sc.next();
            switch(response){
                case "1":
                    updateRoomType(type);
                    break;
                    
                case "2":
                    try {
                        roomSessionBean.deleteRoomType(type);
                    } catch (RoomTypeNotFoundException ex) {  //also catch room occupied exception when you make it
                        System.err.println(ex.getMessage());
                    }
                    break;
            }
        }
    }
    
    private void updateRoomType(String type){
        System.out.println("Enter updated description for " + type);
        String newDescription = sc.nextLine();
        System.out.println("Enter updated amenities for " + type);
        String newAmenities = sc.nextLine();
        System.out.println("Enter updated bed types for " + type);
        String newBeds = sc.nextLine();
        System.out.println("Enter updated capacity for " + type);
        Integer newCap = sc.nextInt();

        try{
            roomSessionBean.updateRoomType(type, newDescription, newBeds, newCap, newAmenities);
        }catch(RoomTypeNotFoundException e){
            System.err.println(e.getMessage());
        }        
    }
    
    private List<RoomTypeEntity> viewAllRoomTypes(){
        System.out.println("All Room Types :");
        List<RoomTypeEntity> roomTypes = roomSessionBean.retrieveListOfRoomTypes();
        int i = 0;
        for(RoomTypeEntity r: roomTypes){
            System.out.println("(" + i + ")" + r.getTypeName());
            i++;
        }
        
        return roomTypes;
    }
    
    
    
    private void roomManagement(){
        while(true){
            System.out.println("(1) Create new Room");
            System.out.println("(2) Delete Room");
            System.out.println("(3) View All Rooms");
            System.out.println("(4) Return");
            
            String response = sc.next();
            switch(response){
                case "1":
                    
                    break;
                    
                case "2":
                    
                    break;
                    
                case "3":
                    
                    break;
                    
                case "4":
                    return;
                                      
                default:
                    System.err.println("Please input a valid command.");
            }
            
        }
    }
    
    private void viewExceptionReport(){
        
    }
}
