import { User } from "./user.model";

export interface Post {
    id?: any;
    postedBy?: User;
    creationDate?: Date;
    content: string;
    group?: number;
    isDeleted?: boolean;
    isEditing: boolean; // Stanje izmjene sadržaja
    updatedContent: string; // Ažurirani sadržaj
    isUpdating: boolean; // Stanje ažuriranja
    showComments: boolean; // Stanje
    comments: Comment[];
}

export interface Comment {
    id?: number;
    user?: User;
    text: string;
    timestamp?: string;
}