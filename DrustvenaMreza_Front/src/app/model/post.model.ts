import { Group } from "./group.model";
import { User } from "./user.model";

export interface Post {
    id?: any;
    postedBy?: User;
    creationDate?: Date;
    content: string;
    isDeleted?: boolean;
    isEditing: boolean; // Stanje izmjene sadržaja
    updatedContent: string; // Ažurirani sadržaj
    isUpdating: boolean; // Stanje ažuriranja
    showComments: boolean; // Stanje
    comments: Comment[];
    reactions: Reaction[];
    selectedReactions: [];
    group?: Group;
}

export interface Comment {
    id?: any;
    user?: User;
    text: string;
    timestamp?: string;
    isEditing?: boolean;
    updatedText: string;
    isUpdating: boolean;
    reactions: Reaction[];
}

export interface Reaction {
    id?: any;
    madeBy?: User;
    type: string;
}