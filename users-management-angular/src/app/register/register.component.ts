import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UsersService } from '../users.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  
  formData: any = {
    name: '',
    email: '',
    password: '',
    role: '',
    city: ''
  };

  errorMessage: string = '';

  constructor(
    private readonly userService: UsersService,
    private readonly router: Router
  ) { }

  async handleSubmit(){
    if(!this.formData.name || !this.formData.email || !this.formData.password ||!this.formData.role || !this.formData.city ){
      this.showError("Please fill in all fields");
      return;
    }

    // confirm registration with user
    const confirmRegistration = confirm("Are you sure want to register this user?");
    if(!confirmRegistration){
      return;
    }

    try{
      const token = localStorage.getItem('token');
      if(!token){
        throw new Error("No token found");
      }

      const response = await this.userService.register(this.formData, token);
      if(response.statusCode === 200){
        this.router.navigate(['/users']);
      } else {
        this.showError(response.message)
      }

    } catch (error: any){
      this.showError(error.message);
    }
  }

  showError(message: string){
    this.errorMessage = message;
    setTimeout(() => {
      this.errorMessage = ''; // clear the error message after the specified duration
    }, 3000)
  }

}
