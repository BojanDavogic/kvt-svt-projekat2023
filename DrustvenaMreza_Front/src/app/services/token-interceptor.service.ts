import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class TokenInterceptorService implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const item = localStorage.getItem('user');
    const decodedItem = item ? JSON.parse(item) : null;

    if (decodedItem && decodedItem.token) {
      const cloned = req.clone({
        setHeaders: {
          Authorization: `Bearer ${decodedItem.token}`
        }
      });

      return next.handle(cloned);
    } else {
      return next.handle(req);
    }
  }
}
