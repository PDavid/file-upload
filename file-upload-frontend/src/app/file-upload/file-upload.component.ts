import {Component} from '@angular/core';
import {FileUploadService} from './file-upload.service';
import {finalize} from 'rxjs/operators';
import {HttpEvent, HttpEventType, HttpProgressEvent} from '@angular/common/http';
import {Subscription} from 'rxjs';

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

    <div class="progress">

      <mat-progress-bar class="progress-bar" mode="determinate"
                        [value]="uploadProgress" *ngIf="uploadProgress">

      </mat-progress-bar>

      <button mat-icon-button aria-label="Cancel file upload"
              class="cancel-upload"
              (click)="cancelUpload()"
              *ngIf="uploadProgress">
        <mat-icon>delete_forever</mat-icon>
      </button>
    </div>
  `,
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent {

  fileName = '';
  uploadProgress?: number;
  uploadSub?: Subscription;

  constructor(private uploadService: FileUploadService) {}

  onFileSelected(event: Event): void {
    const input = event?.target as HTMLInputElement;

    const file: File | null = input?.files ? input.files[0] : null;

    if (file) {
      this.fileName = file.name;

      const formData = new FormData();
      formData.append('file', file);

      this.uploadSub = this.uploadService.upload(formData)
        .pipe(
          finalize(() => this.reset())
        )
        .subscribe((httpEvent: HttpEvent<object>) => {
          if (httpEvent.type === HttpEventType.UploadProgress) {
            const progressEvent: HttpProgressEvent = httpEvent as HttpProgressEvent;
            if (progressEvent.total) {
              this.uploadProgress = Math.round(100 * (progressEvent.loaded / progressEvent.total));
            }
          }
        });
    }
  }

  cancelUpload(): void {
    this.uploadSub?.unsubscribe();
    this.reset();
  }

  reset(): void {
    this.uploadProgress = undefined;
    this.uploadSub = undefined;
  }
}
