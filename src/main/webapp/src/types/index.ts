export interface IUser {
  id: number
  firstName: string
  lastName: string
  age: number
}

export interface IRootState {
  users: IUser[]
  usersError: string | null
  usersLoading: boolean
  usersRequested: boolean
}
