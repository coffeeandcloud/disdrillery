package export

import (
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/model"
	"github.com/xitongsys/parquet-go-source/local"
	"github.com/xitongsys/parquet-go/parquet"
	"github.com/xitongsys/parquet-go/writer"
	"log"
)

type Exporter interface {
	Export(output string)
}

type ParquetExporter struct {
	parquetWriter *writer.ParquetWriter
}

func GetInstance() *ParquetExporter {
	return &ParquetExporter{}
}

func GetParquetWriter(output string, model interface{}) *writer.ParquetWriter {
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

func (exporter *ParquetExporter) SetWriter(parquetWriter *writer.ParquetWriter) *ParquetExporter {
	exporter.parquetWriter = parquetWriter
	return exporter
}

func (exporter *ParquetExporter) Export(data interface{}) {
	if exporter.parquetWriter == nil {
		log.Fatal("Please set parquet writer before exporting parquet file.")
		return
	}
	if v, isType := data.([]model.CommitVertex); isType {
		log.Printf("%d commits", len(v))
		for _, cv := range v {
			if err := exporter.parquetWriter.Write(cv); err != nil {
				log.Fatal("Write error", err)
			}
		}

		if err := exporter.parquetWriter.WriteStop(); err != nil {
			log.Fatal("Write Stop error", err)
		}
	} else if v, isType := data.([]model.CommitEdge); isType {
		log.Printf("%d commits", len(v))
		for _, cv := range v {
			if err := exporter.parquetWriter.Write(cv); err != nil {
				log.Fatal("Write error", err)
			}
		}

		if err := exporter.parquetWriter.WriteStop(); err != nil {
			log.Fatal("Write Stop error", err)
		}
	}
	log.Println("Wrote vertex file successfully.")
}
