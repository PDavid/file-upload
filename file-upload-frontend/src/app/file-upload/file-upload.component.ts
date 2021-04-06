import { Component, OnInit } from '@angular/core';
import {FileUploadService} from './file-upload.service';
import {catchError, map} from 'rxjs/operators';
import {HttpErrorResponse, HttpEvent, HttpEventType} from '@angular/common/http';
import {of} from 'rxjs';
import {FormBuilder, FormControl, Validators} from '@angular/forms';

@Component({
  selector: 'fup-file-upload',
  template: `
    <input type='file' class='file-input'
           (change)='onFileSelected($event)' #fileUpload>

    <div class='file-upload'>

      {{fileName || 'No file uploaded yet.'}}

      <button mat-mini-fab color='primary' class='upload-btn'
              (click)='fileUpload.click()'>
        <mat-icon>attach_file</mat-icon>
      </button>
    </div>
  `,
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent implements OnInit {

  fileName = '';
  fileControl: FormControl;

  constructor(private uploadService: FileUploadService,
              private fb: FormBuilder) {
    this.fileControl = this.fb.control('', Validators.required);
  }

  ngOnInit(): void {
  }

  onFileSelected(event: Event): void {
    console.log('onFileSelected() invoked.', event?.target);

    const input = event?.target as HTMLInputElement;

    const file: File | null = input?.files ? input.files[0] : null;

    if (file) {
      this.fileName = file.name;

      const formData = new FormData();
      formData.append('file', file);

      this.uploadService.upload(formData)
        /*.pipe(
          map((event: HttpEvent<object>) => {
            switch (event.type) {
              case HttpEventType.UploadProgress:
                file.progress = Math.round(event.loaded * 100 / event.total);
                break;
              case HttpEventType.Response:
                return event;
            }
          }),
          catchError((error: HttpErrorResponse) => {
            file.inProgress = false;
            return of(`Upload failed: ${file.name}`);
          }))*/
        .subscribe((httpEvent: HttpEvent<any>) => {
          if (typeof (httpEvent) === 'object') {
            console.log(httpEvent);
          }
        });
    }
  }
}
