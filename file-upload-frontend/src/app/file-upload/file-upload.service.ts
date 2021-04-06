import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  constructor(private httpClient: HttpClient) {}

  public upload(formData: FormData): Observable<HttpEvent<object>> {

    return this.httpClient.post<any>('http://localhost:8080/api/upload', formData, {
      reportProgress: true,
      observe: 'events'
    });
  }
}
