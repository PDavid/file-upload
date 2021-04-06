import { Component } from '@angular/core';

@Component({
  selector: 'fup-root',
  template: `
    <mat-toolbar>
      <span>Excel viewer</span>
    </mat-toolbar>
    <fup-file-upload></fup-file-upload>
  `,
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
}
