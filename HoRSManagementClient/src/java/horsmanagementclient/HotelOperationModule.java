/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.RoomSessionBeanRemote;
import entity.EmployeeEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.*;
import util.enumeration.StatusEnum;
import util.exception.RoomNotFoundException;
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
        
        ArrayList<RoomTypeEntity> roomRanks = roomSessionBean.getRoomRanks();
        int i = 0;
        System.out.println("Select the room ranking position to insert the new room in (Smaller number = more premium)");
        for(RoomTypeEntity r: roomRanks){
            System.out.println("(" + i + ") " + r.getTypeName());
            i++;
        }
        System.out.println("(" + i + ") Least Premium");
        int newRank = sc.nextInt();
        if(newRank >= i){
            i = roomRanks.size(); //least premium
        }else if (newRank < 0){
            i = 0; //most premium
        }
        
        roomSessionBean.createNewRoomType(newTypeName, newDescription, newBeds, capacity, newAmenities, i);
        
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
                        if(roomSessionBean.deleteRoomType(type)){
                            System.out.println("Room Type Deleted");
                        }else{
                            System.out.println("Room Type Currently In Use - Room Type marked as DISABLED");
                            System.out.println("Please try again when room type is not in use any more");
                        }
                    } catch (RoomTypeNotFoundException ex) {
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
            System.out.println("(2) Update Room");
            System.out.println("(3) Delete Room");
            System.out.println("(4) View All Rooms");
            System.out.println("(5) Return");
            
            String response = sc.next();
            switch(response){
                case "1":
                    createNewRoom();
                    break;
                    
                case "2":
                    updateRoom();
                    break;
                    
                case "3":
                    
                    break;
                    
                case "4":
                    viewAllRooms();
                    break;
                    
                case "5":
                    return;
                                      
                default:
                    System.err.println("Please input a valid command.");
            }
            
        }
    }
    
    private void createNewRoom(){
        try {
            System.out.println("Enter Floor Number of New Room");
            Integer floor = sc.nextInt();
            System.out.println("Enter Unit Number of New Room");
            Integer unit = sc.nextInt();
            System.out.println("Select Room Type:");
            List<RoomTypeEntity> roomTypes = viewAllRoomTypes();
            int typeSelection = sc.nextInt();
            String typeName = roomTypes.get(typeSelection).getTypeName();
            roomSessionBean.createNewRoom(floor, unit, typeName);
            System.out.println("New Room " + floor + "-" + unit +" Created");
        } catch (RoomTypeNotFoundException | NullPointerException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
    private void updateRoom(){
        System.out.println("Enter room floor number");
        String floor = sc.next();
        System.out.println("Enter room unit number");
        String unit = sc.next();
        
        String roomNumber = floor + "-" + unit;
        try{
            System.out.println(roomSessionBean.viewRoomDetails(roomSessionBean.retrieveRoomByRoomNumber(roomNumber)));
            System.out.println("Enter new room type:");
            List<RoomTypeEntity> roomTypes = viewAllRoomTypes();
            int typeNum =  sc.nextInt();
            RoomTypeEntity roomType = roomTypes.get(typeNum);
            System.out.println("Enter new room status \n(1)AVAILABLE \n(2)DISABLED");
            StatusEnum status;
            switch(sc.next()){
                case "1":
                    status = StatusEnum.AVAILABLE;
                    break;
                case "2":
                    status = StatusEnum.DISABLED;
                    break;
                default:
                    System.err.println("Not an available status");
                    updateRoom(); //redo
                    return;
            }
            roomSessionBean.updateRoom(roomNumber, roomType.getTypeName(), status);
            
        }catch(RoomNotFoundException | NullPointerException | RoomTypeNotFoundException e){
            System.err.println(e.getMessage());
            return;
        }
        
        
        
    }
    
    private void viewAllRooms(){
        List<RoomEntity> rooms = roomSessionBean.retrieveAllRooms();
        System.out.println("All Rooms:");
        for(RoomEntity room: rooms){
            System.out.println(roomSessionBean.viewRoomDetails(room));
        }
    }
    
    private void viewExceptionReport(){
        
    }
}
