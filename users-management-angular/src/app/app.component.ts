import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavComponent } from './nav/nav.component';
import { FooterComponent } from './footer/footer.component';
import { UsersService } from './users.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavComponent, FooterComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'users-management-angular';

  isAuthenticated = false;

  constructor(private userService: UsersService){}

  nngOnInit(): void {
    this.isAuthenticated = this.userService.isAuthentication();
  }
}
