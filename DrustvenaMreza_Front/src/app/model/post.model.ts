import { Group } from "./group.model";
import { User } from "./user.model";

export interface Post {
    id?: any;
    postedBy?: User;
    creationDate?: Date;
    title: string;
    content: string;
    isDeleted?: boolean;
    isEditing: boolean;
    updatedTitle: string;
    updatedContent: string;
    isUpdating: boolean;
    showComments: boolean;
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
    showReplies: boolean;
    replies: Comment[];
}

export interface Reaction {
    id?: any;
    madeBy?: User;
    type: string;
}