/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */

function createTableFromJSON(data) {
    var html = "<table><tr><th>Category</th><th><tr><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";
    return html;

}


function initDB() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
              $("#ajaxContent").html("Successful Initialization");
        } else if (xhr.status !== 200) {
             $("#ajaxContent").html("Error Occured");
        }
    };

    xhr.open('GET', 'InitDB');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function showMostPopular(){
    var xhr = new XMLHttpRequest();
            xhr.open('GET', 'GetMostPopular', true);
        
        xhr.onload = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                var data = JSON.parse(xhr.responseText);
                document.getElementById('mp_car').value = data.mp_car;
                document.getElementById('mp_motorbike').value = data.mp_motorbike;
                document.getElementById('mp_bike').value = data.mp_bike;
                document.getElementById('mp_scooter').value = data.mp_scooter;
            }
        };

        xhr.send();
}

function getServiceCost() {
    var startTime = document.getElementById("start").value;
    var endTime = document.getElementById("end").value;

    //console.log("start and end: " + startTime + endTime);

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            var resultValue = response.result;
            var message = "Total cost: " + resultValue + " euros.";

            //console.log("Result from servlet: " + response.result);

            document.getElementById("costResult").innerHTML = message;
        }
    };
    xhr.open('GET', "ServiceCost?startTime=" + startTime + "&endTime=" + endTime, true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function showRentalStats(){
    var xhr = new XMLHttpRequest();
            xhr.open('GET', 'GetRentalStats', true);
        
        xhr.onload = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                var data = JSON.parse(xhr.responseText);
                document.getElementById('rent_stats_car').value = data.rent_stats_car;
                document.getElementById('rent_stats_motorbike').value = data.rent_stats_motorbike;
                document.getElementById('rent_stats_bike').value = data.rent_stats_bike;
                document.getElementById('rent_stats_scooter').value = data.rent_stats_scooter;
            }
        };

        xhr.send();
}

function validateUsername(){
    let form= document.getElementById("myForm");
    let form_data=new FormData(form);
    
    
    var username=document.getElementById("username").value;
   
    var xhr=new XMLHttpRequest();
    xhr.onload= function(){
        
       if(xhr.status!==200){
            
            $("#username_error").html("Username already taken!!!!");
            $("#username_error").show();
            $("#submit_button").prop('disabled',true);
            
        }else{
          $("#username_error").hide();
          $("#submit_button").prop('disabled',false);
        }
    };
    xhr.open('GET','UsernameCheck'+ '?username=' + username, true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
        
}

function checkUserforRent() {
    let form = document.getElementById("myForm");
    let form_data = new FormData(form);


    var username = document.getElementById("username").value;

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {

        if (xhr.status !== 200) {

            $("#userRentError").show();
            $("#userRentError").html("Username doesn't exist!");
            $("#submit_button").prop('disabled', true);


        } else {
            $("#userRentError").hide();
            $("#submit_button").prop('disabled', false);
        }
    };
    xhr.open('GET', 'UserRentCheck' + '?username=' + username, true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();

}

function getAllCars() {
    var count = 1;
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {

        if (xhr.status !== 200) {

            $("#car_infos").show();
            $("#car_infos").html("NO cars!!!!");


        } else {
            $("#car_infos").show();
            $("#check_cars_title").hide();
            $("#check_cars").hide();
            var array = JSON.parse(xhr.responseText);
            console.log(array);

            for (var i = 0; i <= 10; i++) {
                document.getElementById("break").appendChild(document.createElement("br"));
            }


            for (var i in array) {
                var jsonObject = JSON.parse(array[i]);
                $("#car_infos").append(count + ") Registration Number: " + jsonObject.reg_no + " Rented by: " + jsonObject.f_name + " " + jsonObject.l_name + "\n");
                $("#car_infos").append(document.createElement("br"));
                count = count + 1;
            }
        }
    };
    xhr.open('GET', 'GetAllVehs?type=car', true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();



}

function getAllMotors() {
    var count = 1;
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {

        if (xhr.status !== 200) {

            $("#motor_infos").show();
            $("#motor_infos").html("NO motors!!!!");


        } else {
            $("#motor_infos").show();
            $("#check_motors_title").hide();
            $("#check_motors").hide();
            var array = JSON.parse(xhr.responseText);
            console.log(array);

            for (var i = 0; i <= 10; i++) {
                document.getElementById("break").appendChild(document.createElement("br"));
            }

            for (var i in array) {
                var jsonObject = JSON.parse(array[i]);
                $("#motor_infos").append(count + ") Registration Number: " + jsonObject.reg_no + " Rented by: " + jsonObject.f_name + " " + jsonObject.l_name + "\n");
                $("#motor_infos").append(document.createElement("br"));
                count = count + 1;
            }
        }
    };
    xhr.open('GET', 'GetAllVehs?type=motorbike', true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();



}

function getAllBikes() {
    var count = 1;
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {

        if (xhr.status !== 200) {

            $("#bike_infos").show();
            $("#bike_infos").html("NO bikes!!!!");


        } else {
            $("#bike_infos").show();
            $("#check_bikes_title").hide();
            $("#check_bikes").hide();
            var array = JSON.parse(xhr.responseText);
            console.log(array);

            for (var i = 0; i <= 10; i++) {
                document.getElementById("break").appendChild(document.createElement("br"));
            }

            for (var i in array) {
                var jsonObject = JSON.parse(array[i]);
                $("#bike_infos").append(count + ") Registration Number: " + jsonObject.reg_no + " Rented by: " + jsonObject.f_name + " " + jsonObject.l_name + "\n");
                $("#bike_infos").append(document.createElement("br"));
                count = count + 1;
            }
        }
    };
    xhr.open('GET', 'GetAllVehs?type=bike', true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();



}

function getAllSkates() {
    var count = 1;
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {

        if (xhr.status !== 200) {

            $("#skate_infos").show();
            $("#skate_infos").html("NO skates!!!!");


        } else {
            $("#skate_infos").show();
            $("#check_skate_title").hide();
            $("#check_skates").hide();
            var array = JSON.parse(xhr.responseText);
            console.log(array);

            for (var i = 0; i <= 10; i++) {
                document.getElementById("break").appendChild(document.createElement("br"));
            }

            for (var i in array) {
                var jsonObject = JSON.parse(array[i]);
                $("#skate_infos").append(count + ") Registration Number: " + jsonObject.reg_no + " Rented by: " + jsonObject.f_name + " " + jsonObject.l_name + "\n");
                $("#skate_infos").append(document.createElement("br"));
                count = count + 1;
            }
        }
    };
    xhr.open('GET', 'GetAllVehs?type=skate', true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();



}

function getRented() {
    var count = 1;
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {

        if (xhr.status !== 200) {

            $("#rentedvehs").html("Cant find any vehicle");


        } else {
            $("#rentedvehs").show();
            var array = JSON.parse(xhr.responseText);
            console.log(array);
            var container = document.getElementById("rentedvehs");
            var radio, label;
            for (var i in array) {

                radio = document.createElement("input");
                radio.type = "radio";
                radio.id = "reg_no";
                radio.name = "reg_no"; // Set the same name for radio buttons to create a radio button group
                radio.value = array[i].reg_no;

                label = document.createElement("label");
                label.appendChild(radio);

                label.appendChild(document.createTextNode(count + ") username: " + array[i].username + " / reg no: " + array[i].reg_no + " / total cost: " + array[i].total_cost + " / rent date: " + array[i].rent_date + " / rent hour: " + array[i].rent_hour
                        + " / return date: " + array[i].return_date + " / return hour: " + array[i].return_hour));



                container.appendChild(label);
//                container.appendChild(document.createElement("br"));


                count++;
            }
        }
    };
    xhr.open('GET', 'GetRented');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function getRentedVehiclesStatus() {
    var startTime = document.getElementById("start").value;
    var endTime = document.getElementById("end").value;

    //console.log("start and end: " + startTime + endTime);

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            var resultValue = response.result;
            var message = "Rental status: " + resultValue + " vehicles rented in this time period.";

            //console.log("Result from servlet: " + response.result);

            document.getElementById("statusResult").innerHTML = message;
        }
    };
    xhr.open('GET', "RentedStatus?startTime=" + startTime + "&endTime=" + endTime, true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function getRentedIncome() {
    var startTime = document.getElementById("start").value;
    var endTime = document.getElementById("end").value;

    //console.log("start and end: " + startTime + endTime);

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            var resultValue = response.result;
            var message = "Total income: " + resultValue + " euros.";

            //console.log("Result from servlet: " + response.result);

            document.getElementById("incomeResult").innerHTML = message;
        }
    };
    xhr.open('GET', "RentedIncome?startTime=" + startTime + "&endTime=" + endTime, true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function getCarIncome() {

    //console.log("start and end: " + startTime + endTime);

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            var resultValue = response.result;
            $("#car_income").show();
            var message = "Car income: " + resultValue + " euros.";

            //console.log("Result from servlet: " + response.result);

            document.getElementById("car_income").innerHTML = message;
        }
    };
    xhr.open('GET', "VehsIncome?type=car", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function getMotorIncome() {

    //console.log("start and end: " + startTime + endTime);

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            var resultValue = response.result;
            $("#motor_income").show();
            var message = "Motorbike income: " + resultValue + " euros.";

            //console.log("Result from servlet: " + response.result);

            document.getElementById("motor_income").innerHTML = message;
        }
    };
    xhr.open('GET', "VehsIncome?type=motorbike", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function getBikeIncome() {

    //console.log("start and end: " + startTime + endTime);

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            var resultValue = response.result;
            $("#bike_income").show();
            var message = "Bike income: " + resultValue + " euros.";

            //console.log("Result from servlet: " + response.result);

            document.getElementById("bike_income").innerHTML = message;
        }
    };
    xhr.open('GET', "VehsIncome?type=bike", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function getSkateIncome() {

    //console.log("start and end: " + startTime + endTime);

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            var resultValue = response.result;
            $("#skate_income").show();
            var message = "Skate income: " + resultValue + " euros.";

            //console.log("Result from servlet: " + response.result);

            document.getElementById("skate_income").innerHTML = message;
        }
    };
    xhr.open('GET', "VehsIncome?type=skate", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}


function get_All_Available_Cars(){
    var count=1;
    var xhr=new XMLHttpRequest();
    
    xhr.onload= function(){
        
       if(xhr.status!==200){
            
           $("#car_infos").show(); 
           $("#car_infos").html("NO cars!!!!");
           
            
        }else{
          $("#car_infos").show(); 
          $("#check_cars_title").hide();
          $("#check_cars").hide();
          var array = JSON.parse(xhr.responseText);
          console.log(array);
          
     
        for (var i in array) {
          $("#car_infos").append(count+") Registration Number: " + array[i].reg_no + " / Car_type: " + array[i].car_type + " / Km: " + array[i].km + " / Colour: " + array[i].colour + " / Model: " + array[i].model + " / Brand: " + array[i].brand
                + " / Passenger Number: " + array[i].passenger_num + " / Daily rent cost: " + array[i].daily_rent_cost + " / Daily_insurance cost: " + array[i].daily_insurance_cost + "\n");
                $("#car_infos").append(document.createElement("br"));
                count=count+1;
            }
        }
    };
    xhr.open('GET','GetCars', true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
    
   
   
}



function get_All_Available_Motors(){
    
    var count=1;
    var xhr=new XMLHttpRequest();
    xhr.onload= function(){
        
    if(xhr.status!==200){

       $("#motor_infos").show();
       $("#motor_infos").html("Error!!!!");


     }else{
            $("#motor_infos").show();
            $("#check_motors_title").hide();
            var array = JSON.parse(xhr.responseText);
            console.log(array);
            for(var i=0;i<=10;i++){
                document.getElementById("break").appendChild(document.createElement("br"));             
            }
             count=1;        
             for (var i in array) {
               $("#motor_infos").append(count+")Registration Number: " + array[i].reg_no + " / Km: " + array[i].km + " / Colour: " + array[i].colour + " / Model: " + array[i].model + " / Brand: " + array[i].brand
                 +" / Daily rent cost: " + array[i].daily_rent_cost + " / Daily_insurance cost: " + array[i].daily_insurance_cost + "\n");
              $("#motor_infos").append(document.createElement("br"));
              count=count+1;
         }
     }
    };
    xhr.open('GET','GetVehicles?type=motorbike', true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
    
}

function get_All_Available_Bikes(){
    var count=1;
    var xhr=new XMLHttpRequest();
    xhr.onload= function(){
        
    if(xhr.status!==200){

       $("#bike_infos").show(); 
       $("#bike_infos").html("Error!!!!");


    }else{
       $("#bike_infos").show(); 
       $("#check_bikes_title").hide();
     
       var array = JSON.parse(xhr.responseText);
       console.log(array);

       for(var i=0;i<=10;i++){
               document.getElementById("break").appendChild(document.createElement("br"));             
          }
            
     for (var i in array) {
       $("#bike_infos").append(count+")Registration Number: " + array[i].reg_no + " / Km: " + array[i].km + " / Colour: " + array[i].colour + " / Model: " + array[i].model + " / Brand: " + array[i].brand
             +" / Daily rent cost: " + array[i].daily_rent_cost + " / Daily_insurance cost: " + array[i].daily_insurance_cost + "\n");
              $("#bike_infos").append(document.createElement("br"));
              count=count+1;
         }
     }
    };
    xhr.open('GET','GetVehicles?type=bike', true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function get_All_Available_Skates(){
    var count=1;
    var xhr=new XMLHttpRequest();
    
    xhr.onload= function(){

      if(xhr.status!==200){

         $("#skate_infos").show(); 
         $("#skate_infos").html("Error!!!!");


       }else{
           
         $("#skate_infos").show(); 
         $("#check_skates_title").hide();
         
         for(var i=0;i<=10;i++){
               document.getElementById("break").appendChild(document.createElement("br"));             
          }
          
         var array = JSON.parse(xhr.responseText);
         console.log(array);

       count=1;        
       for (var i in array) {
         $("#skate_infos").append(count+")Registration Number: " + array[i].reg_no + " / Km: " + array[i].km + " / Colour: " + array[i].colour + " / Model: " + array[i].model + " / Brand: " + array[i].brand
               +" / Daily rent cost: " + array[i].daily_rent_cost + " / Daily_insurance cost: " + array[i].daily_insurance_cost + "\n");
                $("#skate_infos").append(document.createElement("br"));
                count=count+1;
           }
       }
   };
   xhr.open('GET','GetVehicles?type=skate', true); //dinw ta dedomena sto ? part
   xhr.setRequestHeader("Content-type", "application/json");
   xhr.send();
}

function validateReg_no(){
    let form= document.getElementById("myForm");
    let form_data=new FormData(form);
    
    
    var reg_no=document.getElementById("reg_no").value;

    var xhr=new XMLHttpRequest();
    xhr.onload= function(){
        
       if(xhr.status!==200){
            
            $("#reg_no_error").html("There is already a vehicle with this Register number!!!! / add a new reg number or you wont be able to submit!");
            $("#reg_no_error").show();
            $("#submit_button").prop('disabled',true);
            
        }else{
          $("#reg_no_error").hide();
          $("#submit_button").prop('disabled',false);
        }
    };
    xhr.open('GET','Reg_noCheck'+ '?reg_no=' + reg_no , true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
        
}

function CheckDriversName(){
        let form= document.getElementById("myForm");
    let form_data=new FormData(form);
    
    
    var username=document.getElementById("username").value;
    var driver_fname=document.getElementById("driver_fname").value;
    var driver_lname=document.getElementById("driver_lname").value;

    var xhr=new XMLHttpRequest();
    xhr.onload= function(){
        
       if(xhr.status!==200){
            
            $("#SameDriverError").show(); 
            $("#SameDriverError").html("There is already a rented vehicle with this driver so please choose a new driver! <br> OR <br> If its your first time here then the driver you entered is not you and his driver's license number is required");   
            document.getElementById("driver_lic_no").required=true;
            $("#driver_lic").show();
  
            
        }else{
            $("#SameDriverError").hide(); 
            $("#driver_lic").hide();
            document.getElementById("driver_lic_no").required=false;
        }
    };
    xhr.open('GET','CheckDriver'+ '?driver_fname=' + driver_fname + "&driver_lname=" + driver_lname + "&username=" + username, true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}


function show_price(value,id){ //show max price that customer choose
    document.getElementById(id).innerHTML="Price=" + value +"&euro;";
}


function get_All_Available_Cars_For_Rent(daily_rent_cost,date){
    var count=1;
    var xhr=new XMLHttpRequest();
    xhr.onload= function(){
        
       if(xhr.status!==200){
            
          $("#car_infos").html("Cant find any car");
           
            
        }else{
          $("#car_infos").show(); 
          $("#car-btn").hide();
//          $("#check").hide();
          var array = JSON.parse(xhr.responseText);
          console.log(array);
          var container = document.getElementById("car_infos");
          var radio,label;
            for (var i in array) {

                radio = document.createElement("input");
                radio.type = "radio";
                radio.id= "reg_no";
                radio.name = "reg_no"; // Set the same name for radio buttons to create a radio button group
                radio.value = array[i].reg_no;

                label = document.createElement("label");
                label.appendChild(radio);
                label.appendChild(document.createTextNode(count+") Car_type: " + array[i].car_type + " / Km: " + array[i].km + " / Colour: " + array[i].colour + " / Model: " + array[i].model + " / Brand: " + array[i].brand
                    + " / Passenger Number: " + array[i].passenger_num + " / Daily rent cost: " + array[i].daily_rent_cost + " / Daily_insurance cost: " + array[i].daily_insurance_cost));

                container.appendChild(label);
//                container.appendChild(document.createElement("br"));
                
                count++;
            }
        }
    };
    xhr.open('GET','GetRentCars?daily_rent_cost=' + daily_rent_cost +"&date=" +date, true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
 }
 
 
 function get_All_Available_Motors_For_Rent(daily_rent_cost,date){
    var count=1;
    var xhr=new XMLHttpRequest();
    xhr.onload= function(){
        
       if(xhr.status!==200){
            
          $("#motor_infos").html("Error!!!!");
           
            
        }else{
          $("#motor_infos").show(); 
          $("#motor-btn").hide();

          var array = JSON.parse(xhr.responseText);
          var container = document.getElementById("motor_infos");
          var radio,label;
            for (var i in array) {

                radio = document.createElement("input");
                radio.type = "radio";
                radio.id= "reg_no";
                radio.name = "reg_no"; // Set the same name for radio buttons to create a radio button group
                radio.value = array[i].reg_no;

                label = document.createElement("label");
                label.appendChild(radio);
                label.appendChild(document.createTextNode(count+")Km: " + array[i].km + " / Colour: " + array[i].colour + " / Model: " + array[i].model + " / Brand: " + array[i].brand
                    + " / Daily rent cost: " + array[i].daily_rent_cost + " / Daily_insurance cost: " + array[i].daily_insurance_cost));

                container.appendChild(label);
//                container.appendChild(document.createElement("br"));
                
                count++;
            }
        }
    };
    xhr.open('GET','GetRentVehs?daily_rent_cost=' + daily_rent_cost +"&date=" +date +"&type=motorbike", true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
 }
 
 
 function get_All_Available_Bikes_For_Rent(daily_rent_cost,date){
    var count=1;
    var xhr=new XMLHttpRequest();
    xhr.onload= function(){
        
       if(xhr.status!==200){
            
          $("#bike_infos").html("Error!!!!");
           
            
        }else{
          $("#bike_infos").show(); 
          $("#bike-btn").hide();
          var array = JSON.parse(xhr.responseText);
          var container = document.getElementById("bike_infos");
          var radio,label;
            for (var i in array) {

                radio = document.createElement("input");
                radio.type = "radio";
                radio.id= "reg_no";
                radio.name = "reg_no"; // Set the same name for radio buttons to create a radio button group
                radio.value = array[i].reg_no;

                label = document.createElement("label");
                label.appendChild(radio);
                label.appendChild(document.createTextNode(count+") Km: " + array[i].km + " / Colour: " + array[i].colour + " / Model: " + array[i].model + " / Brand: " + array[i].brand
                    + " / Daily rent cost: " + array[i].daily_rent_cost + " / Daily_insurance cost: " + array[i].daily_insurance_cost));

                container.appendChild(label);
//                container.appendChild(document.createElement("br"));
                
                count++;
            }
        }
    };
    xhr.open('GET','GetRentVehs?daily_rent_cost=' + daily_rent_cost +"&date=" +date +"&type=bike", true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
 }
 
 function get_All_Available_Skates_For_Rent(daily_rent_cost,date){
    var count=1;
    var xhr=new XMLHttpRequest();
    xhr.onload= function(){
        
       if(xhr.status!==200){
            
          $("#skate_infos").html("Error!!!!");
           
            
        }else{
          $("#skate_infos").show(); 
          $("#skate-btn").hide();
//          $("#check").hide();
          var array = JSON.parse(xhr.responseText);
          console.log(array);
          var container = document.getElementById("skate_infos");
          var radio,label;
            for (var i in array) {

                radio = document.createElement("input");
                radio.type = "radio";
                radio.id= "reg_no";
                radio.name = "reg_no"; // Set the same name for radio buttons to create a radio button group
                radio.value = array[i].reg_no;

                label = document.createElement("label");
                label.appendChild(radio);
                label.appendChild(document.createTextNode(count+") / Km: " + array[i].km + " / Colour: " + array[i].colour + " / Model: " + array[i].model + " / Brand: " + array[i].brand
                    + " / Daily rent cost: " + array[i].daily_rent_cost + " / Daily_insurance cost: " + array[i].daily_insurance_cost));

                container.appendChild(label);
//                container.appendChild(document.createElement("br"));
                
                count++;
            }
        }
    };
    xhr.open('GET','GetRentVehs?daily_rent_cost=' + daily_rent_cost +"&date=" +date +"&type=skate", true); //dinw ta dedomena sto ? part
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
 }