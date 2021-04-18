export interface FileUploadResponse {
  id: string;
}

export interface StatusResponse {
  status: Status;
}

export enum Status {
  UPLOADING = 'UPLOADING',
  UPLOAD_ERROR = 'UPLOAD_ERROR',
  CONVERTING = 'CONVERTING',
  CONVERSION_ERROR = 'CONVERSION_ERROR',
  CONVERTED = 'CONVERTED',
  UNKNOWN = 'UNKNOWN'
}
