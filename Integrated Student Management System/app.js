const API_URL = "http://localhost:8080/api/students";

//create a student
async function createStudent(studentData) {
  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(studentData),
    });
    if (!response.ok) {
      alert("Student added successfully");
    }
    else{
      alert("Failed to add student");
    }

   }
  catch (error) {
      console.error("Error creating student:", error);
      alert("An error occurred while adding the student.");
    }
    
    document.getElementById("studentForm").addEventListener("submit", function(e){
      e.preventDefault();
      
      const id = document.getElementById("studentId").value;
      const studentData = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        email: document.getElementById("email").value
      };
      createStudent(studentData);
    });
  }