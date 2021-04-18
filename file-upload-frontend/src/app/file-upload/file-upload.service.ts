import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent} from '@angular/common/http';
import {Observable} from 'rxjs';
import {StatusResponse} from './upload.model';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  readonly baseUrl = 'http://localhost:8080/api/';

  constructor(private httpClient: HttpClient) {}

  public upload(formData: FormData): Observable<HttpEvent<object>> {
    return this.httpClient.post<any>(this.baseUrl + 'upload', formData, {
      reportProgress: true,
      observe: 'events'
    });
  }

  public status(id: string): Observable<StatusResponse> {
    return this.httpClient.get<StatusResponse>(this.baseUrl + `status/${id}`);
  }
}
