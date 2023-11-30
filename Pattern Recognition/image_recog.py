import cv2
import numpy as np

class Shape:
    PI = np.pi

    def __init__(self, area, perimeter):
        self.area = area
        self.perimeter = perimeter

    def calculate_r(self):
        return (4 * self.PI * self.area) / (self.perimeter * self.perimeter)

    def recognize_object(self):
        r = self.calculate_r()

        if np.abs(r - (self.PI / 4)) < 1e-1:
            return "Square"
        else:
            return "Circular"

def compute_perimeter(label, labeled_image):
    mask = (labeled_image == label).astype(np.uint8)

    contours, _ = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    perimeter = 0
    for contour in contours:
        perimeter += cv2.arcLength(contour, closed=True)

    return perimeter

def main():
    # Load the image
    image = cv2.imread('e.jpg', cv2.IMREAD_GRAYSCALE)

    if image is None:
        print("Error: Image not found!")
        return

    # Threshold the image
    _, binary_image = cv2.threshold(image, 20, 255, cv2.THRESH_BINARY)

    # Apply connected components labeling
    _, labeled_image, _, _ = cv2.connectedComponentsWithStats(binary_image, connectivity=8)

    # Compute attributes and recognize objects
    num_labels = labeled_image.max() + 1
    for label in range(1, num_labels):
        area = np.sum(labeled_image == label)
        perimeter = compute_perimeter(label, labeled_image)

        shape = Shape(area, perimeter)
        object_type = shape.recognize_object()

        print(f"Object {label}: {object_type}")

if __name__ == "__main__":
    main()
