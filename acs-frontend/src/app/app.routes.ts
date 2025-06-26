import { Routes } from '@angular/router';
import { QrPageComponent } from './compoment/QrPage/qrPage.component';

export const routes: Routes = [
  { path: '', redirectTo: '/qr', pathMatch: 'full' },
  { path: 'qr', component: QrPageComponent }
];