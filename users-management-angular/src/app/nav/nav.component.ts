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
      // Inscreva-se para ouvir as mudanças de estado de autenticação
      this.userService.authStatus$.subscribe(authenticated => {
      this.isAuthentication = authenticated;
      this.isAdmin = this.userService.isAdmin();
      this.isUser = this.userService.isUser();
    });
  }

  logout():void{
    this.userService.logOut();  // Limpa os dados do usuário
    this.router.navigate(['/login']);  // Redireciona para a página de login
  }
}
