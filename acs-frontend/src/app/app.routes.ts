import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { QrPageComponent } from './compoment/QrPage/qrPage.component'; // ← 記得引入你的元件


export const routes: Routes = [
  { path: '', redirectTo: '/qr', pathMatch: 'full' },   // ⬅ 加在這裡
  { path: 'qr', component: QrPageComponent }            // ⬅ QR 頁面路由
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}