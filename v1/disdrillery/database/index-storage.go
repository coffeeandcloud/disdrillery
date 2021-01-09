package index

import (
	"encoding/json"
	"io/ioutil"
	"log"
	"os"
	"path/filepath"
)

type Meta struct {
	Providing string
	File      string
}

type IndexStorage struct {
	indexFile  IndexFile
	workingDir string
}

type IndexFile struct {
	Version       int16  `json:"version"`
	StorageFormat string `json:"storageFormat"`
	RepositoryUrl string `json:"repositoryName"`
	Meta          []Meta `json:"metaData"`
}

var indexStorageInstance *IndexStorage

func getDefaultIndexConfig() IndexFile {
	return IndexFile{
		Version:       1,
		StorageFormat: "parquet",
	}
}

func (f *IndexStorage) writeIndexFile(index IndexFile) {
	filename := f.workingDir + string(filepath.Separator) + "index.json"
	file, _ := json.MarshalIndent(index, "", "\t")
	err := ioutil.WriteFile(filename, file, 0644)
	if err != nil {
		log.Fatal(err)
	}
}

func (f *IndexStorage) UpdateIndexFile() {
	filename := f.workingDir + string(filepath.Separator) + "index.json"
	file, _ := json.MarshalIndent(f.indexFile, "", "\t")
	err := ioutil.WriteFile(filename, file, 0644)
	if err != nil {
		log.Fatal(err)
	}
	log.Println("Index file updated.")
}

func (f *IndexStorage) createWorkingDirectory(outputDir string) {
	// TODO delete outputDir if it already exists
	err := os.MkdirAll(f.GetDataDir(), os.ModePerm)
	if err != nil {
		log.Fatal(err)
	}
	err = os.MkdirAll(f.GetFileDir(), os.ModePerm)
	if err != nil {
		log.Fatal(err)
	}
}

func (f *IndexStorage) Init(repoName string) *IndexStorage {
	f.workingDir = repoName
	f.indexFile = getDefaultIndexConfig()
	f.createWorkingDirectory(repoName)
	f.writeIndexFile(f.indexFile)
	return f
}

func (f *IndexStorage) SetMetaInfos(metaInfos []Meta) *IndexStorage {
	f.indexFile.Meta = metaInfos
	return f
}

func (f *IndexStorage) GetDataDir() string {
	return f.workingDir + string(filepath.Separator) + "index"
}

func (f *IndexStorage) GetFileDir() string {
	return f.workingDir + string(filepath.Separator) + "content"
}

func (f *IndexStorage) GetIndexFile() *IndexFile {
	return &f.indexFile
}

func GetInstance() *IndexStorage {
	if indexStorageInstance == nil {
		indexStorageInstance = &IndexStorage{}
	}
	return indexStorageInstance
}
