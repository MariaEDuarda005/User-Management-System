import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { UsersService } from '../users.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{

  profileInfo: any;
  errorMessage: string = '';

  constructor(private readonly userService: UsersService,
    private readonly router: Router
  ){}

  async ngOnInit() {
    try{
      const token = localStorage.getItem('token')
      if(!token){
        throw new Error("No token found")
      }

      this.profileInfo = await this.userService.getYourProfile(token);
    } catch (error: any) {
      this.showError(error.message)
    }
  }

  updateProfile(id: string){
    this.router.navigate(['/update', id])
  }

  showError(mess: string){
    this.errorMessage = mess;
    setTimeout(() => {
      this.errorMessage = ''
    }, 3000)
  }

}
