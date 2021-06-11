FROM golang:alpine

WORKDIR /usr/local/disdrillery
COPY . ./
ENV GO111MODULE=on
ENV CGO_ENABLED=0
ENV GOOS=linux
RUN ["go", "build", "-o", "/bin/disdrillery", "v1/cmd/main.go"]
ENTRYPOINT [ "/bin/disdrillery" ]