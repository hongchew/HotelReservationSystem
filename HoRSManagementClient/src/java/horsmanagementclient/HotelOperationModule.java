/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.RoomSessionBeanRemote;
import entity.EmployeeEntity;
import entity.ExceptionReportEntity;
import entity.RoomEntity;
import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import util.enumeration.RateTypeEnum;
import util.enumeration.StatusEnum;
import util.exception.LastAvailableRateException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author Hong Chew
 */
public class HotelOperationModule {

    private RoomSessionBeanRemote roomSessionBean;
    
    private EmployeeEntity currentEmployee;
    private final Scanner sc = new Scanner(System.in);
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public HotelOperationModule() {
    }

    public HotelOperationModule(RoomSessionBeanRemote roomSessionBean, EmployeeEntity currentEmployee) {
        this.roomSessionBean = roomSessionBean;
        this.currentEmployee = currentEmployee;
    }
    
    
    public void runHotelOperationModuleOperationsManager(){
        
        while(true){
            System.out.println("\n****Welcome to the Hotel Operations Module****");
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
            System.out.println("\n****Room Type Management****");
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
                    System.out.println("\n***Returning to home page***\n");
                    return;
                                      
                default:
                    System.err.println("\nPlease input a valid command.\n");
            }
            
        }
    }
    
    private void createNewRoomType(){
        System.out.println("Enter name of new Room Type");
        sc.nextLine();
        String newTypeName = sc.nextLine();
        
        System.out.println("Enter 140 characters description for " + newTypeName);
        String newDescription = sc.nextLine();
        
        System.out.println("Enter types of bed available in " + newTypeName);
        String newBeds = sc.nextLine();
        
        System.out.println("Enter capacity of " + newTypeName);
        Integer capacity = sc.nextInt();
        
        System.out.println("Enter amenities available in " + newTypeName);
        sc.nextLine();
        String newAmenities = sc.nextLine();
        
        List<RoomTypeEntity> roomRanks = roomSessionBean.getRoomRanks();
        int i = 0;
        System.out.println("Select the room ranking position to insert the new room in (Smaller number = more premium)");
        for(i = 0; i < roomRanks.size(); i++){
            System.out.println("(" + i + ") " + roomRanks.get(i).getTypeName());
        }
        
        /*for(RoomTypeEntity r: roomRanks){
            System.out.println("(" + i + ") " + r.getTypeName());
            i++;
        }*/
        System.out.println("(" + i + ") Least Premium");
        int newRank = sc.nextInt();
        if(newRank >= i){
            i = roomRanks.size(); //least premium
        }else if (newRank < 0){
            i = 0; //most premium
        }
        
        Long roomTypeId = roomSessionBean.createNewRoomType(newTypeName, newDescription, newBeds, capacity, newAmenities, i);
        
        try {
            System.out.print("Enter Normal Rate per night for " + newTypeName + " $");
            double normalRate = sc.nextDouble();
            roomSessionBean.createNewNormalRate(newTypeName + " Normal Rate", new BigDecimal(normalRate), new Date(), null, roomTypeId);
            
            System.out.print("Enter Published Rate per night for " + newTypeName + " $");
            double publishedRate = sc.nextDouble();            
            roomSessionBean.createNewPublishedRate(newTypeName + " Published Rate", new BigDecimal(publishedRate), new Date(), null, roomTypeId);
        } catch (RoomTypeNotFoundException e) {
            System.out.println(e.getMessage());
        }
        
        System.out.println("\n" + newTypeName + " created successfully!\n");
    }
    
    private void viewRoomTypeDetails(){
        List<RoomTypeEntity> roomTypes = viewAllRoomTypes();
        if(roomTypes.isEmpty()){
            System.out.println("\nNo Room Types Available\n");
            return;
        }
        System.out.println("Input Room Type number to view/edit details or to delete");
        int typeNum = sc.nextInt();
        if(typeNum < 0 || typeNum >= roomTypes.size()){
            System.out.println("Invalid number, returning to main menu");
            return;
        }else{
            try {
                String typeName = roomTypes.get(typeNum).getTypeName();
                System.out.println(roomSessionBean.viewRoomTypeDetails(typeName));
                
                System.out.println("(1) Edit Details");
                System.out.println("(2) Delete Room Type");
                String response = sc.next();
                switch(response){
                    case "1":
                        updateRoomType(typeName);
                        break;
                        
                    case "2":
                        try {
                            if(roomSessionBean.deleteRoomType(typeName)){
                                System.out.println("\n****Room Type Deleted****\n");
                            }else{
                                System.out.println("Room Type Currently In Use - Room Type marked as DISABLED");
                                System.out.println("Please try again when room type is not in use any more");
                            }
                        } catch (RoomTypeNotFoundException ex) {
                            System.err.println(ex.getMessage());
                        }
                        break;
                }
            } catch (RoomTypeNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    private void updateRoomType(String typeName){
        System.out.println("Enter updated description for " + typeName);
        sc.nextLine();
        String newDescription = sc.nextLine();
        System.out.println("Enter updated amenities for " + typeName);
        String newAmenities = sc.nextLine();
        System.out.println("Enter updated bed types for " + typeName);
        String newBeds = sc.nextLine();
        System.out.println("Enter updated capacity for " + typeName);
        Integer newCap = sc.nextInt();

        try{
            roomSessionBean.updateRoomType(typeName, newDescription, newBeds, newCap, newAmenities);
            System.out.println("\n****Room Type Successfully Updated****\n");
        }catch(RoomTypeNotFoundException e){
            System.err.println(e.getMessage());
        }        
    }
    
    private List<RoomTypeEntity> viewAllRoomTypes(){
        System.out.println("\n****All Room Types****");
        List<RoomTypeEntity> roomTypes = roomSessionBean.retrieveListOfRoomTypes();
        int i = 0;
        for(RoomTypeEntity r: roomTypes){
            System.out.println("(" + i + ")" + r.getTypeName());
            i++;
        }
        System.out.println("****End of list****\n");
        return roomTypes;
    }
    
    
    
    private void roomManagement(){
        while(true){
            System.out.println("\n****Room Type Management****");
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
                    deleteRoom();
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
            System.out.println("\n****New Room " + floor + "-" + unit +" Created****");
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
    
    private void deleteRoom(){
        System.out.println("Enter room floor number");
        String floor = sc.next();
        System.out.println("Enter room unit number");
        String unit = sc.next();
        
        String roomNumber = floor + "-" + unit;
        
        try{
            if(roomSessionBean.deleteRoom(roomNumber)){
                System.out.println(roomNumber + " deleted successfully.");
            }else{
                System.out.println(roomNumber + " is currently in use. The room had been disabled instead.");
            }
        }catch(RoomNotFoundException e){
            System.err.println(e.getMessage());
            return;
        }
    }
    
    private void viewAllRooms(){
        List<RoomEntity> rooms = roomSessionBean.retrieveAllRooms();
        System.out.println("****All Rooms****\n");
        for(RoomEntity room: rooms){
            System.out.println(roomSessionBean.viewRoomDetails(room));
        }
        System.out.println("\n****End of list****\n");
    }
    
    private void viewExceptionReport(){
        try {
            System.out.println("Enter Date of Report to generate (DD/MM/YYYY)");
            String dateString = sc.next();
            Date date = dateFormat.parse(dateString);
            
            List<ExceptionReportEntity> report = roomSessionBean.getListOfExceptionReportsByDate(date);
            if(report.isEmpty()){
                System.out.println("****No Exception Report.****");
            }else{
                System.out.println("****Exception Reports****");
                for(ExceptionReportEntity e:report){
                    System.out.println(e.getErrorReport());
                }
                System.out.println("\n****End of list****\n");
            }            
        } catch (ParseException ex) {
            System.err.println("Invalid date format. Please try again.");
        }
    }
    
    
    public void runHotelOperationModuleSalesManager(){
        
        while(true){
            System.out.println("\n****Welcome to the Hotel Operations Module (Sales)****");
            System.out.println("(1) Create new Room Rate");
            System.out.println("(2) View/Delete/Update Room Rate Details");
            System.out.println("(3) View All Room Rates");
            System.out.println("(4) Return");
            String response = sc.next();
            switch(response){
                case "1":
                    createNewRate();
                    break;
                    
                case "2":
                    viewRoomRateDetails();
                    break;
                    
                case "3":
                    viewAllRoomRates();
                    break;
                    
                case "4":
                    return;
 
                default:
                    System.err.println("Please enter a valid command");
            }           
        }
    }    
    
    private void createNewRate(){
        
        System.out.println("\n****Create New Room Rate****");
        List<RoomTypeEntity> roomTypesList = viewAllRoomTypes();
        System.out.println("Select Room type");
        int roomTypeIndex = sc.nextInt();
        RoomTypeEntity roomType;
        if(roomTypeIndex < 0 || roomTypeIndex >= roomTypesList.size()){
            System.err.println("Invalid Room Type.");
            return;
        }else{
            roomType = roomTypesList.get(roomTypeIndex);
        }
        
        System.out.println("Enter name of new rate:");
        sc.nextLine();
        String rateName = sc.nextLine();
        
        System.out.print("Enter rate per night: \n$");
        BigDecimal ratePerNight = new BigDecimal(sc.nextDouble());
        
        System.out.println("Enter Start Date (dd/mm/yyyy): ");
        Date startDate;
        try {
            startDate = dateFormat.parse(sc.next());
        } catch (ParseException ex) {
            System.err.println("Invalid date.");
            return;
        }
        
        System.out.println("Enter End Date (dd/mm/yyyy OR \"-\" if no end date): ");
        String endDateString = sc.next();
        Date endDate;
        if(endDateString.equals("-")){
            endDate = null;
        }else{
            try{
                endDate = dateFormat.parse(endDateString);
            }catch(ParseException ex){
                System.err.println("Invalid date.");
                return;
            }
        }
        
        System.out.println("Enter Room Rate Type: \n(1)Published Rate \n(2)Normal Rate \n(3)Promotion Rate \n(4)Peak Rate");
        try {
            switch (sc.next()) {
                case "1":
                    roomSessionBean.createNewPublishedRate(rateName, ratePerNight, startDate, endDate, roomType.getTypeId());
                    break;
                case "2":
                    roomSessionBean.createNewNormalRate(rateName, ratePerNight, startDate, endDate, roomType.getTypeId());
                    break;
                case "3":
                    roomSessionBean.createNewPromotionRate(rateName, ratePerNight, startDate, endDate, roomType.getTypeId());
                    break;
                case "4":
                    roomSessionBean.createNewPeakRate(rateName, ratePerNight, startDate, endDate, roomType.getTypeId());
            }
        } catch (RoomTypeNotFoundException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("\n****New Room Rate Created****\n");
    }
    
    private void viewRoomRateDetails(){
        List<RoomRateEntity> roomRates = viewAllRoomRates();
        System.out.println("Select number of Room Rate to view details");
        int rateIndex = sc.nextInt();
        if(rateIndex < 0 || rateIndex >= roomRates.size()){
            System.err.println("Invalid number. Please try again");
            return;
        }
        RoomRateEntity roomRate = roomRates.get(rateIndex);
        System.out.println("****Room Rate Details****");
        System.out.println(roomRate.getDetails());
        
        System.out.println();
        System.out.println("(1)Update Room Rate\n(2)Delete Room Rate\n(3)Return");
        switch(sc.next()){
            case "1":
                updateRoomRate(roomRate);
                break;
                
            case "2":
                deleteRoomRate(roomRate);
                break;
            
            case "3":
                return;
            
            default:
                System.err.println("Invalid command");
                return;           
        }
    }
    
    private void updateRoomRate(RoomRateEntity roomRate){
        try {

            System.out.println("\n****Update Room Rate****");
            System.out.print("Enter new rate per night: \n$");
            BigDecimal newRatePerNight = new BigDecimal(sc.nextDouble());
            System.out.println("Enter new start date (dd/mm/yyyy)");
            Date newStartDate = dateFormat.parse(sc.next());
            Date endDate;
            
            //Normal / Published rates will always have no end date
            if(roomRate.getRateType().equals(RateTypeEnum.NORMAL) || roomRate.getRateType().equals(RateTypeEnum.PUBLISHED)){
                endDate = null;
            }else{
                System.out.println("Enter new end date (dd/mm/yyyy OR \"-\" if no end date ");
                String endDateString = sc.next();
                if(endDateString.equals("-")){
                    endDate = null;
                }else{
                    endDate = dateFormat.parse(endDateString);
                }
            }
            
            System.out.println("Select Room Rate Status: \n(1)Disable Room \n(2)Make Room Available");
            StatusEnum status;
            switch(sc.next()){
                case "1":
                    status = StatusEnum.DISABLED;
                    break;
                case "2":
                    status = StatusEnum.AVAILABLE;
                    break;
                default:
                    System.err.println("Invalid status");
                    return;
            }
            
            roomSessionBean.updateRoomRate(roomRate.getRateId(), newRatePerNight, newStartDate, endDate, status);
            
        } catch (ParseException ex) {
            System.err.println("Invalid date entered");
            return;
        } catch (RoomRateNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
    private void deleteRoomRate(RoomRateEntity roomRate){
        try {
            if(roomSessionBean.deleteRoomRate(roomRate.getRateId())){
                System.out.println("Room rate deleted.");
            }else{
                System.out.println("Room rate is currently in use and had been disabled instead.");
            }
        } catch (RoomRateNotFoundException | LastAvailableRateException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    
    private List<RoomRateEntity> viewAllRoomRates(){
        System.out.println("****All Room Rates****\n");
        List<RoomRateEntity> roomRates = roomSessionBean.retrieveAllRoomRates();
        int i = 0;
        for(RoomRateEntity r: roomRates){
            System.out.println("(" + i + ")" + r.getRateName());
            i++;
        }
        System.out.println("\n****End of list****\n");
        
        return roomRates;
    }
    
    
}
