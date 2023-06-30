import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class JwtUtilsService {

  constructor() { }

  decodeToken(token: string): any {
    try {
      const jwtData = token.split('.')[1];
      const decodedJwtJsonData = window.atob(jwtData);
      const decodedJwtData = JSON.parse(decodedJwtJsonData);
      return decodedJwtData;
    } catch (error) {
      console.error('Error decoding JWT token:', error);
      return null;
    }
  }

  getRoles(token: string): string[] {
    const decodedJwtData = this.decodeToken(token);
    if (decodedJwtData && decodedJwtData.role) {
      return [decodedJwtData.role];
    }
    return [];
  }
}
