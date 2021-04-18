import {Component, OnDestroy} from '@angular/core';
import {FileUploadService} from './file-upload.service';
import {finalize, map, share, switchMap, takeUntil, tap} from 'rxjs/operators';
import {HttpEvent, HttpEventType, HttpProgressEvent, HttpResponse} from '@angular/common/http';
import {BehaviorSubject, Subject, Subscription, timer} from 'rxjs';
import {FileUploadResponse, Status, StatusResponse} from './upload.model';

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

    <div class="upload-progress">

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

    <div class="convert-progress" *ngIf="isConverting$ | async">
      Converting, please wait...
      <mat-progress-bar mode="indeterminate"></mat-progress-bar>
    </div>

    <div class="conversion-done" *ngIf="isConverted$ | async">
      Your file was converted successfully.<br/>
      (TODO: Display the file contents!)
    </div>
  `,
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent implements OnDestroy {

  fileName = '';
  uploadProgress?: number;
  uploadSub?: Subscription;

  isConverting$ = new BehaviorSubject<boolean>(false);
  isConverted$ = new BehaviorSubject<boolean>(false);

  private destroyed = new Subject();
  private stopPolling = new Subject();

  constructor(private uploadService: FileUploadService) {}

  onFileSelected(event: Event): void {
    const input = event?.target as HTMLInputElement;

    const file: File | null = input?.files ? input.files[0] : null;

    if (file) {
      this.uploadFile(file);
    }
  }

  private uploadFile(file: File): void {
    this.fileName = file.name;

    const formData = new FormData();
    formData.append('file', file);

    this.uploadSub = this.uploadService.upload(formData)
      .pipe(
        finalize(() => this.resetUpload())
      )
      .subscribe((httpEvent: HttpEvent<object>) => {
        if (httpEvent.type === HttpEventType.UploadProgress) {
          const progressEvent: HttpProgressEvent = httpEvent as HttpProgressEvent;
          if (progressEvent.total) {
            this.uploadProgress = Math.round(100 * (progressEvent.loaded / progressEvent.total));
          }
        }
        else if (httpEvent.type === HttpEventType.Response) {
          const httpResponse: HttpResponse<FileUploadResponse> = httpEvent as HttpResponse<FileUploadResponse>;
          const fileUploadResponse = httpResponse.body;
          if (!fileUploadResponse) {
            // TODO: Handle error!
            console.error('File upload response was null.');
          } else {
            this.startPolling(fileUploadResponse.id);
          }
        }
      });
  }

  startPolling(fileId: string): void {
    this.isConverting$.next(true);

    timer(1, 2000).pipe(
      switchMap(() => this.uploadService.status(fileId)),
      map((response: StatusResponse) => response.status),
      share(),
      takeUntil(this.destroyed),
      takeUntil(this.stopPolling),
      finalize(() => this.isConverting$.next(false)),
    ).subscribe((status) => {
      console.log('status: ', status);
      if (status === Status.CONVERSION_ERROR) {
        // TODO: Handle error!
        console.error('Conversion error.');
        this.stopPolling.next();
      }
      else if (status === Status.UNKNOWN || status === Status.UPLOAD_ERROR) {
        // TODO: Handle error!
        console.error('Status was unknown or upload error.');
        this.stopPolling.next();
      }
      else if (status === Status.CONVERTED) {
        console.log('Yee-haw! Converted.');
        this.stopPolling.next();
        this.isConverted$.next(true);
        // TODO: Display the file contents!
      }
    });
  }

  cancelUpload(): void {
    this.uploadSub?.unsubscribe();
    this.resetUpload();
  }

  resetUpload(): void {
    this.uploadProgress = undefined;
    this.uploadSub = undefined;
  }

  ngOnDestroy(): void {
    this.destroyed.next();
    this.cancelUpload();
  }
}
