import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class UsersService {

  private BASE_URL = "http://localhost:1010"

  constructor(private http: HttpClient) { }

  async login(email:string, password:string): Promise<any>{
    const url = `${this.BASE_URL}/auth/login`;
    try{
      const response = await this.http.post<any>(url, {email, password}).toPromise()
      return response;
    }catch(error){
      throw error;
    }
  }

  async register(userData:any, token:string): Promise<any>{
    const url = `${this.BASE_URL}/auth/register`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try{
      const response = await this.http.post<any>(url, userData, {headers}).toPromise()
      return response;
    }catch(error){
      throw error;
    }
  }

  async getAllUsers(token:string): Promise<any>{
    const url = `${this.BASE_URL}/admin/get-all-users`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try{
      const response = await this.http.get<any>(url, {headers}).toPromise()
      return response;
    }catch(error){
      throw error;
    }
  }
  
  async getYourProfile(token:string): Promise<any>{
    const url = `${this.BASE_URL}/adminuser/get-profile`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try{
      const response = await this.http.get<any>(url, {headers}).toPromise()
      return response;
    }catch(error){
      throw error;
    }
  }

  async getUsersById(userId: string, token:string): Promise<any>{
    const url = `${this.BASE_URL}/admin/get-users/${userId}`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try{
      const response = await this.http.get<any>(url, {headers}).toPromise()
      return response;
    }catch(error){
      throw error;
    }
  }

  async deleteUser(userId: string, token:string): Promise<any>{
    const url = `${this.BASE_URL}/admin/delete/${userId}`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try{
      const response = await this.http.delete<any>(url, {headers}).toPromise()
      return response;
    }catch(error){
      throw error;
    }
  }

  async updateUser(userId: string, userData: any, token:string): Promise<any>{
    const url = `${this.BASE_URL}/admin/update/${userId}`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try{
      const response = await this.http.put<any>(url, userData, {headers}).toPromise()
      return response;
    }catch(error){
      throw error;
    }
  }

  /* Authentications methods */
  logOut():void{
    if(typeof window !== 'undefined' && localStorage){
      localStorage.removeItem('token')
      localStorage.removeItem('role')
    }
  }

  isAuthentication(): boolean{
    if(typeof window !== 'undefined' && localStorage){
      const token = localStorage.getItem('token');
      // (!!) são usados em JavaScript para converter um valor em um valor booleano (true ou false).
      return !!token;
    }
    return false;
  }

  isAdmin(): boolean{
    if(typeof window !== 'undefined' && localStorage){
      const role = localStorage.getItem('role');
      return role === 'ADMIN'
    }
    return false;
  }

  isUser(): boolean{
    if(typeof window !== 'undefined' && localStorage){
      const role = localStorage.getItem('role');
      return role === 'USER'
    }
    return false;
  }

  checkAuthentication(): boolean {
    const token = localStorage.getItem('token');
    return !!token; // Retorna true se o token existir
  }

}
