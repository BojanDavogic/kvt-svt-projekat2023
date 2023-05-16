import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from './home-page/home-page.component';
import { GroupListComponent } from './group-list/group-list.component';
import { GroupComponent } from './group/group.component';
import { GroupDetailsComponent } from './group-details/group-details.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: 'home-page', component: HomePageComponent},
  { path: 'groups', component: GroupComponent, children: [
    { path: '', component: GroupListComponent }
  ] },
  { path: 'groups/:id', component: GroupDetailsComponent },
  { path: 'group-list', component: GroupListComponent },
  { path: '', redirectTo: '/home-page', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
