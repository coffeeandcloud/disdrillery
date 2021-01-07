package export

import (
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/model"
	"github.com/xitongsys/parquet-go-source/local"
	"github.com/xitongsys/parquet-go/parquet"
	"github.com/xitongsys/parquet-go/writer"
	"log"
)

type ParquetExporter struct {
}

func getParquetWriter(output string, model interface{}) *writer.ParquetWriter {
	file, err := local.NewLocalFileWriter(output)
	if err != nil {
		log.Fatal(err)
	}
	parquetWriter, err := writer.NewParquetWriter(file, model, 4)
	if err != nil {
		log.Fatal(err)
	}

	parquetWriter.RowGroupSize = 128 * 1024 * 1024
	parquetWriter.PageSize = 8 * 1024
	parquetWriter.CompressionType = parquet.CompressionCodec_SNAPPY
	return parquetWriter
}

func (exporter *ParquetExporter) ExportGeneric(output string, data []interface{}) {
	log.Println(len(data))
}

func (exporter *ParquetExporter) ExportCommitVertex(output string, data *[]model.CommitVertex) {
	parquetWriter := getParquetWriter(output, new(model.CommitVertex))

	/*
		dRef := *data
		v, isType := dRef.([]model.CommitVertex)

		if isType {
			log.Printf("%d commits", len(v))
			for _, cv := range v {
				if err := parquetWriter.Write(cv); err != nil {
					log.Fatal("Write error", err)
				}
			}

			if err := parquetWriter.WriteStop(); err != nil {
				log.Fatal("Write Stop error", err)
			}
		}
	*/
	log.Printf("Got %d vertices.", len(*data))
	for _, cv := range *data {
		if err := parquetWriter.Write(cv); err != nil {
			log.Fatal("Write error", err)
		}
	}

	if err := parquetWriter.WriteStop(); err != nil {
		log.Fatal("Write Stop error", err)
	}
	log.Println("Wrote vertex file successfully to", output)
}

func (exporter *ParquetExporter) ExportCommitEdge(output string, data *[]model.CommitEdge) {
	parquetWriter := getParquetWriter(output, new(model.CommitEdge))
	log.Printf("Got %d edges.", len(*data))
	for _, cv := range *data {
		if err := parquetWriter.Write(cv); err != nil {
			log.Fatal("Write error", err)
		}
	}

	if err := parquetWriter.WriteStop(); err != nil {
		log.Fatal("Write Stop error", err)
	}
	log.Println("Wrote vertex file successfully to", output)

}
