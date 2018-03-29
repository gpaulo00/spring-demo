
export interface User {
  id: number,
  firstName: String,
  lastName: String,
  age: number,
}

export interface RootState {
  users: User[],
  usersError: string|null,
  usersLoading: boolean,
  usersRequested: boolean,
}
