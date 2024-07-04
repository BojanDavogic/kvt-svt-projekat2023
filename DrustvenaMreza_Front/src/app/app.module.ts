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
import { UserService } from './services/user.service';
import { PostService } from './services/post.service';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { ToastrModule } from 'ngx-toastr';
import { SearchPostsComponent } from './search-posts/search-posts.component';
import { SearchGroupComponent } from './search-group/search-group.component';
import { AddRulesComponent } from './add-rules/add-rules.component';
import { HighlightPipe } from './highlight.pipe';


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
    PostComponent,
    UserProfileComponent,
    ChangePasswordComponent,
    SearchPostsComponent,
    SearchGroupComponent,
    AddRulesComponent,
    HighlightPipe,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
  ],
  providers: [GroupService, AuthService, UserService, PostService],
  bootstrap: [AppComponent]
})
export class AppModule { }
