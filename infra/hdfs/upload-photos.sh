#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
IMAGE_DIR="$REPO_ROOT/data/images"
HDFS_DIR="/photos"

if ! command -v hdfs >/dev/null 2>&1; then
  echo "HATA: hdfs komutu bulunamadi."
  exit 1
fi

if [ ! -d "$IMAGE_DIR" ]; then
  echo "HATA: image klasoru bulunamadi: $IMAGE_DIR"
  exit 1
fi

jpg_count=$(find "$IMAGE_DIR" -maxdepth 1 -type f -name '*.jpg' | wc -l)

if [ "$jpg_count" -eq 0 ]; then
  echo "HATA: yuklenecek JPG dosyasi bulunamadi."
  exit 1
fi

echo "HDFS hedef klasoru hazirlaniyor: $HDFS_DIR"
hdfs dfs -mkdir -p "$HDFS_DIR"

echo "Yerel JPG dosyalari yukleniyor..."
find "$IMAGE_DIR" -maxdepth 1 -type f -name '*.jpg' | while read -r file; do
  name="$(basename "$file")"
  echo "Yukleniyor: $name"
  hdfs dfs -put -f "$file" "$HDFS_DIR/$name"
done

echo
echo "Yukleme tamamlandi."
echo "HDFS klasor icerigi:"
hdfs dfs -ls "$HDFS_DIR"