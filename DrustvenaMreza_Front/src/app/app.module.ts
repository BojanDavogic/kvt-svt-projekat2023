import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GroupService } from './services/group.service';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { GroupListComponent } from './group-list/group-list.component';
import { NavbarComponent } from './navbar/navbar.component';
import { GroupComponent } from './group/group.component';
import { HomePageComponent } from './home-page/home-page.component';
import { NewGroupPopupComponent } from './new-group-popup/new-group-popup.component';
import { GroupDetailsComponent } from './group-details/group-details.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { AuthService } from './services/auth.service';
import { EditGroupPopupComponent } from './edit-group-popup/edit-group-popup.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PostComponent } from './post/post.component';



@NgModule({
  declarations: [
    AppComponent,
    GroupListComponent,
    NavbarComponent,
    GroupComponent,
    HomePageComponent,
    NewGroupPopupComponent,
    GroupDetailsComponent,
    LoginComponent,
    RegistrationComponent,
    EditGroupPopupComponent,
    PostComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule
  ],
  providers: [GroupService, AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
