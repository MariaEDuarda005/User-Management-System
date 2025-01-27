import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { UsersService } from '../users.service';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})


export class NavComponent implements OnInit{
  
  isAuthentication: boolean = false;
  isAdmin: boolean = false;
  isUser: boolean = false;

  constructor(private readonly userService: UsersService, 
    private readonly router: Router
  ){}

  ngOnInit(): void {
    // Atualize os estados de autenticação e papel do usuário
    this.checkAuthenticationStatus();
  }

  // Função para atualizar os estados de autenticação e papel
  checkAuthenticationStatus(): void {
    this.isAuthentication = this.userService.isAuthentication();
    this.isAdmin = this.userService.isAdmin();
    this.isUser = this.userService.isUser();
    console.log('Auth:', this.isAuthentication, 'Admin:', this.isAdmin, 'User:', this.isUser);
  }

  logout():void{
    this.userService.logOut();  // Limpa os dados do usuário
    this.checkAuthenticationStatus();  // Atualiza o estado após o logout
    this.router.navigate(['/login']);  // Redireciona para a página de login
  }
}
