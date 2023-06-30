export interface User {
    id?: any;
    sub?: string;
    username: string;
    password: string;
    email: string;
    description?: string;
    lastLogin?: Date;
    firstName: string;
    lastName: string;
    isDelete: boolean;
}
