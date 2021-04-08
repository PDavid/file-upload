import {ChangeDetectionStrategy, Component} from '@angular/core';

@Component({
  selector: 'fup-root',
  template: `
    <mat-toolbar>
      <span>Excel viewer</span>
    </mat-toolbar>
    <div class="container-wrapper">
      <div class="container">
          <fup-file-upload></fup-file-upload>
      </div>
    </div>
  `,
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
}
