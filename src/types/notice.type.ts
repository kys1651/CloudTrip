export interface INotice {
    noticeId: number
    title: string
    content: string
    createdAt: Date
  }

export interface NoticeForm {
    title: string
    content: string
}